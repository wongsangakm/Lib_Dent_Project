package com.example.project.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.model.Book;
import com.example.project.model.BookFavorite;
import com.example.project.repository.BookRepository;
import com.example.project.repository.FavoriteRepository;

@Service
public class BookAnalyticsService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private BookRepository bookRepository;

    public Map<String, Object> getTopBooksWithHeatmap(int limit) {
        List<BookFavorite> allFavs = favoriteRepository.findAll();

        // Top book IDs
        Map<Long, Long> favCounts = allFavs.stream()
            .collect(Collectors.groupingBy(f -> f.getBook().getId(), Collectors.counting()));

        List<Long> topBookIds = favCounts.entrySet().stream()
            .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
            .limit(limit)
            .map(Map.Entry::getKey)
            .toList();

        Map<Long, String> bookIdToTitle = allFavs.stream()
            .filter(fav -> topBookIds.contains(fav.getBook().getId()))
            .collect(Collectors.toMap(
                f -> f.getBook().getId(),
                f -> f.getBook().getBookTitle(),
                (a, b) -> a
            ));

        List<Book> topBooks = bookRepository.findAllById(topBookIds);

        List<Map<String, Object>> topBooksList = topBooks.stream()
            .sorted((b1, b2) -> Long.compare(
                favCounts.getOrDefault(b2.getId(), 0L),
                favCounts.getOrDefault(b1.getId(), 0L)))
            .map(book -> {
                Map<String, Object> m = new HashMap<>();
                m.put("id", book.getId());
                m.put("title", book.getBookTitle());
                m.put("cover", book.getCoverImage());
                m.put("favorites", favCounts.getOrDefault(book.getId(), 0L));
                m.put("status", book.getStatus());
                return m;
            })
            .toList();

        // Heatmap
        Map<String, Map<String, Long>> heatmap = new LinkedHashMap<>();
        for (BookFavorite fav : allFavs) {
            if (fav.getUser().getAcademicField() == null) continue;
            Long bookId = fav.getBook().getId();
            if (!topBookIds.contains(bookId)) continue;

            String field = fav.getUser().getAcademicField().getNameTh();
            String title = bookIdToTitle.get(bookId);
            heatmap.computeIfAbsent(field, k -> new HashMap<>());
            Map<String, Long> row = heatmap.get(field);
            row.put(title, row.getOrDefault(title, 0L) + 1);
        }

        List<String> columns = topBookIds.stream().map(bookIdToTitle::get).toList();
        List<Map<String, Object>> rows = heatmap.entrySet().stream().map(entry -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("name", entry.getKey());
            for (String col : columns) {
                row.put(col, entry.getValue().getOrDefault(col, 0L));
            }
            return row;
        }).toList();

        return Map.of(
            "topBooks", topBooksList,
            "columns", columns,
            "rows", rows
        );
    }

    public Map<Long, Long> getFavoriteCountMap() {
        return favoriteRepository.findAll().stream()
            .filter(f -> f.getBook() != null)
            .collect(Collectors.groupingBy(
                f -> f.getBook().getId(),
                Collectors.counting()
            ));
    }

    public List<Long> getTopBookIds(int limit) {
        return favoriteRepository.findAll().stream()
            .filter(f -> f.getBook() != null)
            .collect(Collectors.groupingBy(f -> f.getBook().getId(), Collectors.counting()))
            .entrySet().stream()
            .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
            .limit(limit)
            .map(Map.Entry::getKey)
            .toList();
    }
}