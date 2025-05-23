package com.example.project.controller;

import com.example.project.service.BookAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "https://requestbooks-dentkku.vercel.app", allowCredentials = "true")
public class AdminAnalyticsController {

    @Autowired
    private BookAnalyticsService analyticsService;

    @GetMapping("/top-books-matrix")
    public ResponseEntity<?> getTopBooksMatrix() {
        return ResponseEntity.ok(analyticsService.getTopBooksWithHeatmap(5));
    }
}
