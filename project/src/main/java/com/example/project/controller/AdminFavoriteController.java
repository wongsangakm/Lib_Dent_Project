package com.example.project.controller;

import com.example.project.model.Book;
import com.example.project.model.BookFavorite;
import com.example.project.repository.BookRepository;
import com.example.project.repository.FavoriteRepository;
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
    @GetMapping("/active-users-by-field")
    public ResponseEntity<?> getActiveUserDistributionByField() {
        List<Object[]> raw = favoriteRepository.countUsersByAcademicField();

        List<Map<String, Object>> result = raw.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", row[0]);   // สาขา
            map.put("value", row[1]);  // จำนวนคน
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

@GetMapping("/favorite-books-by-field")
public ResponseEntity<?> getFavoriteBooksGroupedByField() {
    List<BookFavorite> all = favoriteRepository.findAll();

    // group by สาขา
    Map<String, List<BookFavorite>> grouped = all.stream()
        .filter(fav -> fav.getUser().getAcademicField() != null)
        .collect(Collectors.groupingBy(
            fav -> fav.getUser().getAcademicField().getNameTh()
        ));

    Map<String, List<Map<String, Object>>> result = new HashMap<>();

    for (Map.Entry<String, List<BookFavorite>> entry : grouped.entrySet()) {
        String field = entry.getKey();

        // group by หนังสือ → set ของ user IDs
        Map<String, Set<Long>> bookToUsers = entry.getValue().stream()
            .collect(Collectors.groupingBy(
                fav -> fav.getBook().getBookTitle(),
                Collectors.mapping(fav -> fav.getUser().getId(), Collectors.toSet())
            ));

        // แปลงเป็น list + limit top 10
        List<Map<String, Object>> books = bookToUsers.entrySet().stream()
            .map(e -> {
                Map<String, Object> m = new HashMap<>();
                m.put("name", e.getKey());
                m.put("value", e.getValue().size()); // ✅ จำนวนคน
                return m;
            })
            .sorted((a, b) -> ((Integer) b.get("value")) - ((Integer) a.get("value")))
            .limit(10)
            .collect(Collectors.toList());

        result.put(field, books);
    }

    return ResponseEntity.ok(result);
}


    @GetMapping("/top-books")
    public ResponseEntity<?> getTopBooks() {
        List<BookFavorite> allFavs = favoriteRepository.findAll();
      // รวมจำนวน favorite ตามชื่อหนังสือ
    Map<String, Long> bookCounts = allFavs.stream()
        .collect(Collectors.groupingBy(
            fav -> fav.getBook().getBookTitle(),
            Collectors.counting()
        ));

    // เรียงจากมากไปน้อย
    List<Map<String, Object>> sorted = bookCounts.entrySet().stream()
        .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
        .map(entry -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", entry.getKey());
            map.put("value", entry.getValue());
            return map;
        })
        .toList();

    // เอา Top 10
    List<Map<String, Object>> top10 = sorted.stream().limit(10).toList();

    // รวมยอดที่เหลือ
    long othersTotal = sorted.stream()
        .skip(10)
        .mapToLong(item -> (Long) item.get("value"))
        .sum();

    // ถ้ามีหนังสืออื่นนอกเหนือจาก Top 10
    if (othersTotal > 0) {
        Map<String, Object> others = new HashMap<>();
        others.put("name", "อื่น ๆ");
        others.put("value", othersTotal);
        top10 = new ArrayList<>(top10); // ทำให้ editable
        top10.add(others);
    }

    return ResponseEntity.ok(top10);
    }


@GetMapping("/heatmap/top-books-by-field")
public ResponseEntity<?> getTopBooksByFieldHeatmap() {
    List<BookFavorite> allFavs = favoriteRepository.findAll();

    // Step 1: หาหนังสือ Top 10 โดยรวม (ใช้ bookId เพื่อความแม่นยำ)
    Map<Long, Long> bookCounts = allFavs.stream()
        .collect(Collectors.groupingBy(
            fav -> fav.getBook().getId(),
            Collectors.counting()
        ));

    List<Long> topBookIds = bookCounts.entrySet().stream()
        .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
        .limit(5)
        .map(Map.Entry::getKey)
        .toList();

    // ดึงชื่อหนังสือ Top 10
    Map<Long, String> bookIdToTitle = allFavs.stream()
        .filter(fav -> topBookIds.contains(fav.getBook().getId()))
        .collect(Collectors.toMap(
            fav -> fav.getBook().getId(),
            fav -> fav.getBook().getBookTitle(),
            (a, b) -> a // keep first one if duplicated
        ));

    // Step 2: สร้างตาราง cross-tab field → book → count
    Map<String, Map<String, Long>> heatmap = new LinkedHashMap<>();

    for (BookFavorite fav : allFavs) {
        if (fav.getUser().getAcademicField() == null) continue;

        Long bookId = fav.getBook().getId();
        if (!topBookIds.contains(bookId)) continue;

        String field = fav.getUser().getAcademicField().getNameTh();
        String bookTitle = bookIdToTitle.get(bookId);

        heatmap.computeIfAbsent(field, f -> new HashMap<>());
        Map<String, Long> fieldRow = heatmap.get(field);
        fieldRow.put(bookTitle, fieldRow.getOrDefault(bookTitle, 0L) + 1);
    }

    // Step 3: จัดรูปแบบผลลัพธ์
    List<String> columns = topBookIds.stream()
        .map(bookIdToTitle::get)
        .toList();

    List<Map<String, Object>> rows = new ArrayList<>();

    for (Map.Entry<String, Map<String, Long>> entry : heatmap.entrySet()) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("name", entry.getKey());
        for (String col : columns) {
            row.put(col, entry.getValue().getOrDefault(col, 0L));
        }
        rows.add(row);
    }

    Map<String, Object> response = Map.of(
        "columns", columns,
        "rows", rows
    );

    return ResponseEntity.ok(response);
}

@PutMapping("/mark-popular-requests")
public ResponseEntity<?> markBooksAsPopularRequest() {
    // ดึง ID ของหนังสือทั้งหมดที่ถูกกด favorite อย่างน้อย 1 ครั้ง
    List<Long> bookIds = favoriteRepository.findBookIdsWithFavorites();

    // ดึงหนังสือทั้งหมดตาม ID
    List<Book> books = bookRepository.findAllById(bookIds);

    for (Book book : books) {
        book.setStatus("popular_request");
    }

    bookRepository.saveAll(books);

    return ResponseEntity.ok(Map.of("updated", books.size()));
}


}
