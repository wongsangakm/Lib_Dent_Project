package com.example.project.controller;

import com.example.project.model.Book;
import com.example.project.model.BookFavorite;
import com.example.project.model.User;
import com.example.project.repository.BookRepository;
import com.example.project.repository.FavoriteRepository;
import com.example.project.repository.UserRepository;
import com.example.project.service.EmailService;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth/favorites")

public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;


    // ✅ เพิ่ม favorite (หากยังไม่เคยกด)
  @PostMapping("/{bookId}")
    public ResponseEntity<?> addFavorite(@PathVariable Long bookId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
            System.out.println("💡 userId from session = " + userId);
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        if (favoriteRepository.existsByUserIdAndBookId(userId, bookId)) {
            return ResponseEntity.ok(Map.of("message", "Already favorited"));
        }

        User user = userRepository.findById(userId).orElseThrow();
        Book book = bookRepository.findById(bookId).orElseThrow();

        BookFavorite favorite = new BookFavorite();
        favorite.setUser(user);
        favorite.setBook(book);
        favoriteRepository.save(favorite);

        emailService.sendNotificationToAdmin(book.getBookTitle(), user.getEmail());

        return ResponseEntity.ok(Map.of("success", true));
    }

    // ✅ ตรวจสอบว่า user เคย favorite หรือยัง
    @GetMapping("/check/{bookId}")
    public Map<String, Object> isFavorited(@PathVariable Long bookId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        boolean isFav = userId != null && favoriteRepository.existsByUserIdAndBookId(userId, bookId);
        return Map.of("isFavorited", isFav);
    }

    // ✅ ดูรายการหนังสือทั้งหมดที่ user คนนั้นเคยกด favorite
    @GetMapping
    public ResponseEntity<?> getUserFavorites(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        List<BookFavorite> favs = favoriteRepository.findByUserId(userId);
        List<Book> books = favs.stream().map(BookFavorite::getBook).toList();

        return ResponseEntity.ok(books);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> checkFavorite(@PathVariable Long bookId, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        boolean isFav = favoriteRepository.existsByUserIdAndBookId(userId, bookId);
        return ResponseEntity.ok(Map.of("isFavorited", isFav));
    }


    @GetMapping("/ids")
    public ResponseEntity<?> getFavoriteBookIds(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        List<Long> bookIds = favoriteRepository.findBookIdsByUserId(userId);
        return ResponseEntity.ok(bookIds);
    }

    
}
