package com.example.project.controller;

import com.example.project.repository.FavoriteRepository;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin") 
public class AdminFavoriteController {

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
}
