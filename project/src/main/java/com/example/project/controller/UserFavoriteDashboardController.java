package com.example.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.model.Book;
import com.example.project.model.BookFavorite;
import com.example.project.model.User;
import com.example.project.repository.BookRepository;
import com.example.project.repository.FavoriteRepository;
import com.example.project.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserFavoriteDashboardController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardData(HttpServletRequest request) {
           // ดึง username จาก session
        String username = request.getUserPrincipal().getName(); 
        Optional<User> userOpt = userRepository.findByUsernameIgnoreCase(username);
    
        if (userOpt.isEmpty()) return ResponseEntity.status(401).body("Unauthorized");

        User user = userOpt.get();
        // ดึง favorite ทั้งหมดของ user นี้
        List<BookFavorite> favs = favoriteRepository.findByUserId(user.getId());

        // แปลงเป็น bookId -> Book
        List<Book> books = favs.stream()
                .map(BookFavorite::getBook)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        // รวม favorite count ต่อ book
        Map<Long, Long> favoriteCounts = favoriteRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        f -> f.getBook().getId(),
                        Collectors.counting()
                ));

        // สร้าง response book list
        List<Map<String, Object>> bookList = books.stream().map(book -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", book.getId());
            m.put("title", book.getBookTitle());
            m.put("author", book.getPublisher()); // หรือ field author แท้จริงถ้ามี
            m.put("cover", book.getCoverImage()); // สมมติว่ามี field coverImage
            m.put("status", book.getStatus());
            m.put("favorites", favoriteCounts.getOrDefault(book.getId(), 0L));
            m.put("trendingStatus", 0); // ยังไม่คำนวณจริง
            return m;
        }).toList();

        // รวมสถานะ
        long inShelf = books.stream().filter(b -> "in_shelf".equals(b.getStatus())).count();
        long ordered = books.stream().filter(b -> "ordered".equals(b.getStatus())).count();
        long requested = books.stream().filter(b -> "popular_request".equals(b.getStatus()) || b.getStatus() == null).count();
        // ตรวจเฉพาะหนังสือที่มีอยู่จริงในระบบ
        long booksWithFavs = favoriteRepository.findAll().stream()
            .map(BookFavorite::getBook)
            .filter(Objects::nonNull)
            .map(Book::getId)
            .distinct()
            .count();


        Map<String, Object> summary = Map.of(
                "inShelf", inShelf,
                "ordered", ordered,
                "requested", requested,
                "total", books.size()
        );

        List<Long> userFavoriteIds = books.stream()
        .map(Book::getId)
        .toList();

        return ResponseEntity.ok(Map.of(
                "books", bookList,
                "summary", summary,
                "userFavorites", userFavoriteIds,
                "booksWithFavorites", booksWithFavs
        ));
    }
}
