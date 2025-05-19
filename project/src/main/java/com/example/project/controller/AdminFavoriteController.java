package com.example.project.controller;

import com.example.project.model.Book;
import com.example.project.model.BookFavorite;
import com.example.project.repository.BookRepository;
import com.example.project.repository.FavoriteRepository;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin") 
public class AdminFavoriteController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    /**
     * นับจำนวนผู้ที่ favorite หนังสือแต่ละเล่ม (เฉพาะเล่มที่มีคนกด)
     * @return List of bookId, bookTitle, favoriteCount
     */
    @GetMapping("/favorite-counts")
    public ResponseEntity<?> getFavoriteCounts() {
        List<Object[]> raw = favoriteRepository.countFavoritesByBook();

        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : raw) {
            Map<String, Object> map = new HashMap<>();
            map.put("bookId", row[0]);
            map.put("bookTitle", row[1]);
            map.put("favoriteCount", row[2]);
            result.add(map);
        }

        return ResponseEntity.ok(result);
    }
@GetMapping("/export/favorites")
public ResponseEntity<?> exportByRound(
    @RequestParam int round,
    @RequestParam int year
    
) {
    try {
        Set<Integer> months = switch (round) {
            case 1 -> Set.of(5,6,7,8,9,10,11,12);
            case 2 -> Set.of(1,2);
            case 3 -> Set.of(3,4);
            default -> throw new IllegalArgumentException("Invalid round");
        };

        List<BookFavorite> favs = favoriteRepository.findAll();

        // filter รอบ + ปี
        List<BookFavorite> filteredFavs = favs.stream()
            .filter(fav -> {
                LocalDateTime created = fav.getCreatedAt();
                return created.getYear() == year && months.contains(created.getMonthValue());
            }).toList();

        // group by book
        Map<Long, List<BookFavorite>> grouped = filteredFavs.stream()
            .collect(Collectors.groupingBy(f -> f.getBook().getId()));

        List<Map<String, Object>> result = grouped.entrySet().stream()
            .map(entry -> {
                Book book = entry.getValue().get(0).getBook(); // ดึง book จากรายการแรก
                Map<String, Object> map = new HashMap<>();
                map.put("title", book.getBookTitle());
                map.put("isbn", book.getIsbn());
                map.put("price", book.getPrice());
                map.put("year", book.getYear());
                map.put("bookCover", book.getCoverImage());
                map.put("publisher", book.getPublisher());
                map.put("favorites", entry.getValue().size());
                map.put("edition",book.getEdition());
                List<String> userNames = entry.getValue().stream()
                    .map(fav -> fav.getUser().getFirstName() + " " + fav.getUser().getLastName())
                    .collect(Collectors.toList());

                map.put("users", String.join(", ", userNames)); 
                
                List<String> userNamesEn = entry.getValue().stream()
                    .map(fav -> fav.getUser().getFirstNameEn() + " " + fav.getUser().getLastNameEn())
                    .collect(Collectors.toList());

                map.put("usersEn", String.join(", ", userNamesEn)); 
                
                return map;
            })
            .toList();

        return ResponseEntity.ok(result);

    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error: " + e.getMessage());
    }
}



}
