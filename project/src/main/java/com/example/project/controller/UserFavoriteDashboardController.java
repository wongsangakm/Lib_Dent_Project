package com.example.project.controller;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.model.AdditionalRequest;
import com.example.project.model.Book;
import com.example.project.model.BookFavorite;
import com.example.project.model.User;
import com.example.project.repository.AdditionalRequestRepository;
import com.example.project.repository.BookRepository;
import com.example.project.repository.FavoriteRepository;
import com.example.project.repository.UserRepository;
import com.example.project.service.BookAnalyticsService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserFavoriteDashboardController {

    @Autowired private FavoriteRepository favoriteRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private AdditionalRequestRepository additionalRequestRepository;
    @Autowired private BookAnalyticsService analyticsService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardData(HttpServletRequest request) {
        User user = null;
        List<Long> userFavoriteIds = List.of(); // Default ว่าง

        // ดึงรายการเพิ่มเติมทั้งหมดจากผู้ใช้
        List<AdditionalRequest> additionalBooks = additionalRequestRepository.findAll();
        long additionalRequested = additionalBooks.size();
        long additionalInShelf = additionalBooks.stream().filter(b -> "in_shelf".equals(b.getStatus())).count();
        long additionalOrdered = additionalBooks.stream().filter(b -> "ordered".equals(b.getStatus())).count();
        long additionalPopularRequest = additionalBooks.stream().filter(b -> "popular_request".equals(b.getStatus())).count();

        List<Book> books = List.of();
        try {
            String username = request.getUserPrincipal().getName();
            Optional<User> userOpt = userRepository.findByUsernameIgnoreCase(username);
            if (userOpt.isPresent()) {
                user = userOpt.get();
                List<BookFavorite> favs = favoriteRepository.findByUserId(user.getId());
                userFavoriteIds = favs.stream().map(f -> f.getBook().getId()).distinct().toList();
                books = favs.stream().map(BookFavorite::getBook).filter(Objects::nonNull).distinct().toList();
            }
        } catch (Exception e) {
            // Guest user ไม่มี session
        }

        // นับจำนวน Favorite ต่อหนังสือ
        Map<Long, Long> favoriteCounts = favoriteRepository.findAll().stream()
            .collect(Collectors.groupingBy(f -> f.getBook().getId(), Collectors.counting()));

        // สร้าง book list จากรายการโปรดของ user
        List<Map<String, Object>> bookList = books.stream().map(book -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", book.getId());
            m.put("title", book.getBookTitle());
            m.put("author", book.getPublisher());
            m.put("cover", book.getCoverImage());
            m.put("status", book.getStatus());
            
            m.put("favorites", favoriteCounts.getOrDefault(book.getId(), 0L));
            m.put("trendingStatus", 0); // Placeholder
            return m;
        }).toList();

        // สรุปสถานะของหนังสือที่ user ชื่นชอบ
        long inShelf = books.stream().filter(b -> "in_shelf".equals(b.getStatus())).count();
        long ordered = books.stream().filter(b -> "ordered".equals(b.getStatus())).count();
        long requested = books.stream().filter(b -> "popular_request".equals(b.getStatus()) || b.getStatus() == null).count();

        Map<String, Object> summary = Map.of(
            "inShelf", inShelf,
            "ordered", ordered,
            "requested", requested,
            "total", books.size()
        );

        // นับภาพรวมจากทุกเล่ม + additional
        List<Book> allBooks = bookRepository.findAll();
        long inShelfAll = allBooks.stream().filter(b -> "in_shelf".equals(b.getStatus())).count() + additionalInShelf;
        long orderedAll = allBooks.stream().filter(b -> "ordered".equals(b.getStatus())).count() + additionalOrdered;
        long requestedAll = allBooks.stream().filter(b -> "popular_request".equals(b.getStatus())).count() + additionalPopularRequest;

        Map<String, Object> overallSummary = Map.of(
            "inShelf", inShelfAll,
            "ordered", orderedAll,
            "requested", requestedAll,
            "total", allBooks.size()
        );

        Map<String, Object> additionalSummary = Map.of(
            "count", additionalRequested,
            "inShelf", additionalInShelf,
            "ordered", additionalOrdered,
            "requested", additionalPopularRequest
        );

        // Unique book ที่มีการกดหัวใจ
        long booksWithFavs = favoriteRepository.findAll().stream()
            .map(BookFavorite::getBook)
            .filter(Objects::nonNull)
            .map(Book::getId)
            .distinct()
            .count();

        // ดึง Top 5 หนังสือจาก AnalyticsService
        List<Long> topBookIds = analyticsService.getTopBookIds(5);
        Map<Long, Long> favoriteCountMap = analyticsService.getFavoriteCountMap();
        List<Book> topBooksRaw = bookRepository.findAllById(topBookIds);

        List<Map<String, Object>> topBooks = topBooksRaw.stream()
            .sorted(Comparator.comparingLong(b -> -favoriteCountMap.getOrDefault(b.getId(), 0L)))
            .map(book -> {
                Map<String, Object> m = new HashMap<>();
                m.put("id", book.getId());
                m.put("title", book.getBookTitle());
                m.put("cover", book.getCoverImage());
                m.put("favorites", favoriteCountMap.getOrDefault(book.getId(), 0L));
                m.put("status", book.getStatus());
                return m;
            }).toList();

        return ResponseEntity.ok(Map.of(
            "books", bookList,
            "summary", summary,
            "userFavorites", userFavoriteIds,
            "booksWithFavorites", booksWithFavs,
            "overallSummary", overallSummary,
            "additionalSummary", additionalSummary,
            "topBooks", topBooks
        ));
    }
} 
