package com.example.project.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.project.model.LibraryBook;
import com.example.project.repository.LibraryBookRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class LibraryBookImportService {

    private final LibraryBookRepository repository;

    public LibraryBookImportService(LibraryBookRepository repository) {
        this.repository = repository;
    }

    public void importFromExcel(MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            // 1) สแกนหาแถว Header จริง (ที่มีคำว่า CALLNO, COPY, TITLE, AUTHOR, LANG, PUBBEYEAR)
            int headerRowIndex = -1;
            int callNoColIndex = -1;
            int copyColIndex   = -1;
            int titleColIndex  = -1;
            int authorColIndex = -1;
            int langColIndex   = -1;
            int yearColIndex   = -1;

            for (Row row : sheet) {
                Map<String, Integer> foundCols = new HashMap<>();
                for (Cell cell : row) {
                    if (cell == null) continue;
                    String val = cell.toString().trim();
                    if ("CALLNO".equalsIgnoreCase(val)) {
                        foundCols.put("CALLNO", cell.getColumnIndex());
                    } else if ("COPY".equalsIgnoreCase(val)) {
                        foundCols.put("COPY", cell.getColumnIndex());
                    } else if ("TITLE".equalsIgnoreCase(val)) {
                        foundCols.put("TITLE", cell.getColumnIndex());
                    } else if ("AUTHOR".equalsIgnoreCase(val)) {
                        foundCols.put("AUTHOR", cell.getColumnIndex());
                    } else if ("LANG".equalsIgnoreCase(val)) {
                        foundCols.put("LANG", cell.getColumnIndex());
                    } else if ("PUBBEYEAR".equalsIgnoreCase(val)) {
                        foundCols.put("PUBBEYEAR", cell.getColumnIndex());
                    }
                }

                // ถ้าเจอ “CALLNO”, “COPY”, “TITLE” อย่างน้อย (ถือเป็น header) จะถือว่าเจอแถว header จริง
                if (foundCols.containsKey("CALLNO")
                    && foundCols.containsKey("COPY")
                    && foundCols.containsKey("TITLE")) 
                {
                    headerRowIndex  = row.getRowNum();
                    callNoColIndex  = foundCols.get("CALLNO");
                    copyColIndex    = foundCols.get("COPY");
                    titleColIndex   = foundCols.get("TITLE");
                    authorColIndex  = foundCols.getOrDefault("AUTHOR", -1);
                    langColIndex    = foundCols.getOrDefault("LANG", -1);
                    yearColIndex    = foundCols.getOrDefault("PUBBEYEAR", -1);
                    break;
                }
            }

            if (headerRowIndex < 0) {
                throw new RuntimeException("ไม่พบแถว Header ที่มี CALLNO, COPY, TITLE ในไฟล์ Excel");
            }

            // 2) วนอ่านข้อมูลตั้งแต่แถว headerRowIndex + 1 จนถึง sheet.getLastRowNum()
            for (int r = headerRowIndex + 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null) continue;

                // อ่านค่า callNo, copy, title, author, lang, pubbeyear ตาม index ที่หาได้
                String callNo = (callNoColIndex >= 0)
                    ? getCellValue(row.getCell(callNoColIndex))
                    : null;
                String copy   = (copyColIndex >= 0)
                    ? getCellValue(row.getCell(copyColIndex))
                    : null;
                String title  = (titleColIndex >= 0)
                    ? getCellValue(row.getCell(titleColIndex))
                    : null;

                // ถ้า title = null หรือว่าง ให้ข้าม (ไม่มีข้อมูลหนังสือ)
                if (title == null || title.isEmpty()) {
                    continue;
                }

                String author    = (authorColIndex >= 0)
                    ? getCellValue(row.getCell(authorColIndex))
                    : null;
                String lang      = (langColIndex >= 0)
                    ? getCellValue(row.getCell(langColIndex))
                    : null;
                String pubbeyear = (yearColIndex >= 0)
                    ? getCellValue(row.getCell(yearColIndex))
                    : null;

                // 3) เช็คซ้ำ: title + author + lang + pubbeyear + copy
                boolean exists = repository
          .existsByTitleIgnoreCaseAndAuthorIgnoreCaseAndLangIgnoreCaseAndPubbeyearAndCopy(
                        title, author, lang, pubbeyear, copy
                    );
                if (exists) {
                    // ถ้าเจอซ้ำ (ทุกเงื่อนไขเหมือนกันหมด รวม copy) ให้ข้าม ไม่บันทึกซ้ำ
                    continue;
                }

                // 4) ถ้าไม่ซ้ำ ให้สร้าง entity แล้ว save
                LibraryBook book = new LibraryBook();
                book.setTitle(title);
                book.setAuthor(author);
                book.setLang(lang);
                book.setPubbeyear(pubbeyear);
                book.setCallNo(callNo);
                book.setCopy(copy);

                repository.save(book);
            }

        } catch (Exception e) {
            // ถ้ามี error ให้โยน RuntimeException พร้อมข้อความ เพื่อ Vue จะแสดงได้
            throw new RuntimeException("⚠️ Spring Error: Import failed: " + e.getMessage(), e);
        }
    }

    /**
     * ช่วยอ่านค่าใน Cell แล้วคืนเป็น String
     */
    private String getCellValue(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double d = cell.getNumericCellValue();
                    long l = (long) d;
                    return (d == (double) l) ? String.valueOf(l) : String.valueOf(d);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    FormulaEvaluator evaluator = cell.getSheet()
                        .getWorkbook()
                        .getCreationHelper()
                        .createFormulaEvaluator();
                    CellValue cv = evaluator.evaluate(cell);
                    switch (cv.getCellType()) {
                        case STRING:
                            return cv.getStringValue().trim();
                        case NUMERIC:
                            double d2 = cv.getNumberValue();
                            long l2 = (long) d2;
                            return (d2 == (double) l2) ? String.valueOf(l2) : String.valueOf(d2);
                        case BOOLEAN:
                            return String.valueOf(cv.getBooleanValue());
                        default:
                            return "";
                    }
                } catch (Exception ex) {
                    return "";
                }
            default:
                return "";
        }
    }
}
