package com.example.project.controller;

import com.example.project.model.Book;
import com.example.project.model.BookFavorite;
import com.example.project.model.User;
import com.example.project.payload.request.FavoriteRequest;
import com.example.project.repository.BookRepository;
import com.example.project.repository.FavoriteRepository;
import com.example.project.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:5173")
public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;
    
    // ✅ POST เพิ่ม Favorite
    @PostMapping
    public ResponseEntity<?> addFavorite(@RequestBody FavoriteRequest request) {
        Book book = bookRepository.findById(request.getBookId()).orElseThrow();
        User user = userRepository.findById(1L).orElseThrow(); // 💡 userId ชั่วคราว

        BookFavorite favorite = new BookFavorite();
        favorite.setBook(book);
        favorite.setUser(user); // ✅ ต้องใช้ object

        favoriteRepository.save(favorite);

        return ResponseEntity.ok(Map.of("success", true));
    }


    // ✅ GET เช็คว่าหนังสือเล่มนี้ของ user นี้เคย favorite ไหม
    @GetMapping("/{bookId}")
    public Map<String, Object> isFavorited(@PathVariable Long bookId,
                                           @RequestParam(defaultValue = "1") Long userId) {
        Map<String, Object> response = new HashMap<>();
        boolean favorited = favoriteRepository.existsByUserIdAndBookId(userId, bookId);
        response.put("isFavorited", favorited);
        return response;
    }
}

