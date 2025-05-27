package com.example.project.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handle(Exception ex) {
        ex.printStackTrace(); // ✅ log ไป backend
        return ResponseEntity.badRequest().body("เกิดข้อผิดพลาดจากฝั่งเซิร์ฟเวอร์ กรุณาตรวจสอบข้อมูลที่กรอก");
    }
}
