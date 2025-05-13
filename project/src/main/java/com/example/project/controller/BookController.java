package com.example.project.controller;

import com.example.project.model.Book;
import com.example.project.payload.BookCoverRequest;
import com.example.project.payload.BookDescriptionRequest;
import com.example.project.repository.BookRepository;
import com.example.project.service.BookMetadataScraper;

import jakarta.transaction.Transactional;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth/books")
@CrossOrigin(origins = "http://localhost:5173")
public class BookController {

    private final DataFormatter formatter = new DataFormatter();

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMetadataScraper scraper;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book newBook) {
        Book savedBook = bookRepository.save(newBook);
        return ResponseEntity.ok(savedBook);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file) {
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            Map<String, Integer> columnMap = new HashMap<>();
            int startRow = -1;
    
            // หาหัวตาราง
            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
    
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    String header = formatter.formatCellValue(row.getCell(j)).trim().toLowerCase();

                    System.out.println("Header: [" + header + "]");

                    if (header.contains("isbn")) columnMap.put("isbn", j);
                    if (header.equals("title")) columnMap.put("title", j);
                    if (header.contains("author")) columnMap.put("author", j);
                    if (header.contains("year")|| header.contains("yr.")) columnMap.put("year", j);
                    if (header.contains("price")) columnMap.put("price", j);
                    if (header.contains("url")) columnMap.put("url", j);
                    if (header.contains("publisher")) columnMap.put("publisher", j);
                    if ((header.equalsIgnoreCase("edition") || header.equalsIgnoreCase("ed")) &&
                    !header.contains("editor")) {
                    columnMap.put("edition", j);
                }
                
                }
    
                if (!columnMap.isEmpty()) {
                    startRow = i + 1;
                    break;
                }
            }
    
            for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
    
                Book book = new Book();
                book.setQuantity(0); // ไม่อ่านจาก Excel
                book.setEdition(getCell(row, columnMap, "edition"));
                book.setPublisher(getCell(row, columnMap, "publisher"));
                book.setIsbn(getCell(row, columnMap, "isbn"));
                book.setBookTitle(getRawCell(row, columnMap, "title")); 
                if (columnMap.containsKey("url")) {
                    book.setUrl(getCellString(row.getCell(columnMap.get("url"))));
                } else {
                    book.setUrl(null); // หรือ "" ถ้าคุณไม่อยากให้เป็น null
                }; 
                book.setAuthor(getCell(row, columnMap, "author"));
                book.setYear(getCell(row, columnMap, "year"));

                book.setQuantity(0);
    
                try {
                    String priceRaw = getCell(row, columnMap, "price").replace(",", "").replace("THB", "").trim();
                    book.setPrice(Double.parseDouble(priceRaw));
                } catch (Exception e) {
                    book.setPrice(0.0);
                }
    
                // ตรวจสอบซ้ำ ISBN
                if (book.getIsbn() != null && !book.getIsbn().isEmpty()) {
                    Optional<Book> existing = bookRepository.findByIsbn(book.getIsbn());
                    if (existing.isEmpty()) {
                        bookRepository.save(book); // save ก่อน
                        scraper.fetchAndUpdateBookMetadata(book); // async fetch
                    }
                }
            }
    
            return ResponseEntity.ok("นำเข้าข้อมูลเรียบร้อยแล้ว");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
    
    @Transactional
    @PutMapping("/{isbn}/description")
    public ResponseEntity<?> updateDescriptionByIsbn(@PathVariable String isbn, @RequestBody BookDescriptionRequest request) {
        Optional<Book> bookOpt = bookRepository.findByIsbn(isbn);
        if (bookOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
    
        Book book = bookOpt.get();
        book.setDescription(request.getDescription());
        bookRepository.save(book);
    
        return ResponseEntity.ok("Description updated.");
    }
    
    @Transactional
    @PutMapping("/{isbn}/cover")
    public ResponseEntity<?> updateCoverImageByIsbn(@PathVariable String isbn, @RequestBody BookCoverRequest request) {
        Optional<Book> bookOpt = bookRepository.findByIsbn(isbn);
        if (bookOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Book book = bookOpt.get();
        book.setCoverImage(request.getCoverImage());
        bookRepository.save(book);

        return ResponseEntity.ok("Cover image updated.");
    }

    @PutMapping("/{isbn}/publisher")
    public ResponseEntity<?> updatePublisher(@PathVariable String isbn, @RequestBody Map<String, String> body) {
        String publisher = body.get("publisher");
        Book book = bookRepository.findByIsbn(isbn).orElseThrow();
        book.setPublisher(publisher);
        bookRepository.save(book);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
    
        Book existingBook = optionalBook.get();
        existingBook.setBookTitle(updatedBook.getBookTitle());
        existingBook.setPublisher(updatedBook.getPublisher());
        existingBook.setCoverImage(updatedBook.getCoverImage());
        existingBook.setPrice(updatedBook.getPrice());
        existingBook.setDescription(updatedBook.getDescription());
    
        Book savedBook = bookRepository.save(existingBook);
        return ResponseEntity.ok(savedBook);
    }

    private String getRawCell(Row row, Map<String, Integer> map, String key) {
        if (!map.containsKey(key)) return "";
        Cell cell = row.getCell(map.get(key));
        if (cell == null) return "";
    
        // ✅ ดึงค่าที่พิมพ์ไว้ในเซลล์โดยตรง (ไม่ใช่สูตร)
        if (cell.getCellType() == CellType.STRING) {
            return cell.getRichStringCellValue().getString().trim();
        }
    
        // fallback
        return formatter.formatCellValue(cell).trim();
    }
    
    
    private String getCell(Row row, Map<String, Integer> map, String key) {
        if (!map.containsKey(key)) return "";
        Cell cell = row.getCell(map.get(key));
        return cell == null ? "" : formatter.formatCellValue(cell).trim();
    }
    

    // Helper: Extract string from cell (support HYPERLINK formula + embedded link)
    private String getCellString(Cell cell) {
        if (cell == null) return "";

        if (cell.getHyperlink() != null && cell.getHyperlink().getAddress() != null) {
            return cell.getHyperlink().getAddress();
        }

        if (cell.getCellType() == CellType.FORMULA) {
            String formula = cell.getCellFormula();
            if (formula.toLowerCase().startsWith("hyperlink(")) {
                int openParen = formula.indexOf('(');
                int comma = formula.indexOf(',');
                if (comma > openParen) {
                    String cellRef = formula.substring(openParen + 1, comma).trim();
                    try {
                        Cell refCell = resolveCellReference(cell.getSheet(), cellRef);
                        return formatter.formatCellValue(refCell).trim();
                    } catch (Exception e) {
                        return "UNKNOWN";
                    }
                }
            }
        }

        return formatter.formatCellValue(cell).trim();
    }

    private Cell resolveCellReference(Sheet sheet, String ref) {
        CellReference cellRef = new CellReference(ref);
        Row refRow = sheet.getRow(cellRef.getRow());
        if (refRow == null) return null;
        return refRow.getCell(cellRef.getCol());
    }

    @SuppressWarnings("unused")
    private String getByKey(Row row, Map<String, Integer> columnMap, String key) {
        if (!columnMap.containsKey(key)) return "";
        return getCellString(row.getCell(columnMap.get(key)));
    }

}  