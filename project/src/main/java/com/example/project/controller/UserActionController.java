package com.example.project.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import com.example.project.model.Book;
import com.example.project.model.User;
import com.example.project.repository.UserRepository;
import com.example.project.repository.BookRepository;
import com.example.project.service.UserActionService;

@RestController
@RequestMapping("/api/actions")
public class UserActionController {
    
    @Autowired
    private UserActionService userActionService;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/fav/{bookId}")
    public String addToFavourites(@PathVariable Long bookId, HttpSession session) {
        String username = (String) session.getAttribute("username");
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

        userActionService.recordAction(user, book);
        return "Book added to favourites!";
    }

    @GetMapping("/favourites")
    public List<Book> getUserFavourites(HttpSession session) {
        String username = (String) session.getAttribute("username");
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        return userActionService.getFavouritesByUser(user);
    }

    @PostMapping("/unfav/{bookId}")
    public String removeFromFavourites(@PathVariable Long bookId, HttpSession session) {
        String username = (String) session.getAttribute("username");
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

        userActionService.removeAction(user, book);
        return "Book removed from favourites!";
    }
}
