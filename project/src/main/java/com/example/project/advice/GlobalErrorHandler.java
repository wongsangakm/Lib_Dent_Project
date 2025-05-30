package com.example.project.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle(Exception ex) {
        ex.printStackTrace(); // ✅ log ไป console หรือ Render Logs
        return ResponseEntity
                .badRequest()
                .body("⚠️ Spring Error: " + ex.getMessage());
    }
}
