package com.example.project.controller;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.jdbc.core.JdbcTemplate;
import com.example.project.model.LibraryBook;
import com.example.project.repository.LibraryBookRepository;
import com.example.project.service.LibraryBookImportService;

@RestController
@RequestMapping("/api/library-books")
public class LibraryBookController {
     private final JdbcTemplate jdbcTemplate;
 private final LibraryBookRepository repository;
    private final LibraryBookImportService importService;

    // ✅ รวม constructor เดียว
    public LibraryBookController(
        LibraryBookRepository repository,
        LibraryBookImportService importService,
        JdbcTemplate jdbcTemplate
    ) {
        this.repository = repository;
        this.importService = importService;
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping( value = "/import",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> importExcel(@RequestPart("file") MultipartFile file) {
        importService.importFromExcel(file);
        return ResponseEntity.ok("✅ Import completed");
    }

     @GetMapping("/similar")
    public List<LibraryBook> findSimilarBooks(
        @RequestParam String title,
        @RequestParam(required = false) String author
    ) {
        return repository.findTop5ByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(
            title, author == null ? "" : author
        );
    }

    @GetMapping("/similar-lite")
    public List<LibraryBook> findSLibraryBookControllerimilarLite(@RequestParam String title) {
        return repository.findTop100ByTitleContainingIgnoreCase(title);
    }
@DeleteMapping("/all")
@Transactional
public ResponseEntity<?> deleteAllLibraryBooks() {
 try {
        repository.deleteAllBooksNative();
        jdbcTemplate.execute("ALTER SEQUENCE library_book_id_seq RESTART WITH 1");
        return ResponseEntity.ok("✅ ลบข้อมูลทั้งหมดเรียบร้อยแล้ว");
    } catch (Exception e) {
        return ResponseEntity.badRequest().body("❌ ลบข้อมูลไม่สำเร็จ: " + e.getMessage());
    }
}




}
