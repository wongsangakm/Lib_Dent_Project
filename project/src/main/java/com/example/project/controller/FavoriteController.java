package com.example.project.controller;

import com.example.project.model.Book;
import com.example.project.model.BookFavorite;
import com.example.project.model.User;
import com.example.project.payload.request.FavoriteRequest;
import com.example.project.repository.BookRepository;
import com.example.project.repository.FavoriteRepository;
import com.example.project.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private BookRepository bookRepository;

    // @Autowired
    // private UserRepository userRepository;

    // ✅ POST เพิ่ม Favorite
    @PostMapping
    public ResponseEntity<?> addFavorite(@RequestBody FavoriteRequest request, HttpSession session) {
    User user = (User) session.getAttribute("user");
    if (user == null) { // หากไม่พบ user ใน session หมายถึงยังไม่ได้ล็อกอิน
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("กรุณาล็อกอินก่อนที่จะเพิ่มรายการโปรด");
    }

        Book book = bookRepository.findById(request.getBookId()).orElseThrow();

        BookFavorite favorite = new BookFavorite();
        favorite.setBook(book);
        favorite.setUser(user);

        favoriteRepository.save(favorite);

        return ResponseEntity.ok(Map.of("success", true));
    }

    // ✅ GET เช็คว่าหนังสือเล่มนี้ของ user นี้เคย favorite ไหม
    @GetMapping("/favbooks/{bookId}")
    public ResponseEntity<Map<String, Object>> isFavorited(@PathVariable Long bookId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        boolean favorited = favoriteRepository.existsByUserIdAndBookId(user.getId(), bookId);
        return ResponseEntity.ok(Map.of("isFavorited", favorited));
    }

    // ✅ GET ดึงรายการหนังสือที่ user ชอบ
    @GetMapping
    public ResponseEntity<List<Book>> getFavorites(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(401).build(); // UNAUTHORIZED
        }

        List<BookFavorite> favs = favoriteRepository.findByUserId(user.getId());
        List<Book> books = favs.stream().map(BookFavorite::getBook).toList();

        return ResponseEntity.ok(books); // ✅ ส่งกลับ List<Book>
    }
}
