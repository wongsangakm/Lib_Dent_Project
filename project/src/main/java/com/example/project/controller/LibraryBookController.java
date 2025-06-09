
package com.example.project.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.project.model.LibraryBook;
import com.example.project.repository.LibraryBookRepository;
import com.example.project.service.LibraryBookImportService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
@CrossOrigin(origins = "https://requestbooks-dentkku.vercel.app", allowCredentials = "true")
@RestController
@RequestMapping("/api/library-books")
public class LibraryBookController {

        @PersistenceContext
    private EntityManager entityManager;
 private final LibraryBookRepository repository;
    private final LibraryBookImportService importService;

    // ✅ รวม constructor เดียว
    public LibraryBookController(
        LibraryBookRepository repository,
        LibraryBookImportService importService
    ) {
        this.repository = repository;
        this.importService = importService;
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
    public List<LibraryBook> findSimilarLite(@RequestParam String title) {
        return repository.findTop100ByTitleContainingIgnoreCase(title);
    }
    @DeleteMapping("/all")
    @Transactional
    public ResponseEntity<Void> deleteAllLibraryBooks() {
        try {
            // เพิ่ม flush ก่อน truncate
            entityManager.flush();
            entityManager.clear();
            
            // ใช้คำสั่งเดียวกับที่ทดสอบใน SQL Console
            entityManager.createNativeQuery("TRUNCATE TABLE library_book RESTART IDENTITY CASCADE")
                        .executeUpdate();

                             // Force reset sequence ให้กลับมาเริ่มที่ 1
            entityManager.createNativeQuery("ALTER SEQUENCE library_book_id_seq RESTART WITH 1")
                     .executeUpdate();

            
            // Flush อีกครั้งเพื่อให้แน่ใจ
            entityManager.flush();
            
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error details: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
