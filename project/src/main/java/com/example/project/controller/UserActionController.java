package com.example.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.project.model.Book;
import com.example.project.model.User;
import com.example.project.repository.BookRepository;
import com.example.project.service.UserActionService;

@RestController
@RequestMapping("/api/actions")
public class UserActionController {

    @Autowired
    private UserActionService userActionService;

    @Autowired
    private BookRepository bookRepository;

    // เพิ่มหนังสือที่ผู้ใช้ "fav"
    @PostMapping("/fav/{bookId}")
    public String addToFavourites(@PathVariable Long bookId) {
        // ดึงข้อมูลผู้ใช้ที่ล็อกอินอยู่
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // ค้นหาหนังสือจาก ID ที่ได้รับ
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

        // บันทึกการกระทำ "fav"
        userActionService.recordAction(user, book, "FAVORITE");

        return "Book added to favourites!";
    }

    // แสดงรายการหนังสือที่ผู้ใช้ได้ "fav"
    @GetMapping("/favourites")
    public List<Book> getUserFavourites() {
        // ดึงข้อมูลผู้ใช้ที่ล็อกอินอยู่
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        // คืนค่ารายการหนังสือที่ผู้ใช้ทำการ "fav"
        return userActionService.getFavouritesByUser(user);
    }

    // เพิ่ม API สำหรับการลบรายการ "fav"
    @PostMapping("/unfav/{bookId}")
    public String removeFromFavourites(@PathVariable Long bookId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

        // ลบการกระทำ "fav"
        userActionService.removeAction(user, book, "FAVORITE");

        return "Book removed from favourites!";
    }
}
