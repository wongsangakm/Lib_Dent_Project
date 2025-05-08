package com.example.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.model.User;
import com.example.project.payload.request.LoginRequest;
import com.example.project.service.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpSession session) {
        Optional<User> user = userService.authenticate(request.getUsername(), request.getPassword());
        if (user.isPresent()) {
            session.setAttribute("username", user.get().getUsername());
            session.setAttribute("role", user.get().getRole());

            if (session.getAttribute("favBooks") == null) {
                session.setAttribute("favBooks", new ArrayList<String>());
            }

            Map<String, String> response = new HashMap<>();
            response.put("message", "Login success");
            response.put("username", user.get().getUsername());
            response.put("role", user.get().getRole());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        if (username == null || role == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Map<String, String> response = new HashMap<>();
        response.put("username", username);
        response.put("role", role);
        return ResponseEntity.ok(response);
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/favbooks")
    public List<String> getFavBooks(HttpSession session) {
        List<String> favBooks = (List<String>) session.getAttribute("favBooks");
        return (favBooks != null) ? favBooks : new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    @PostMapping("/favbooks/add")
    public String addFavBook(@RequestBody Map<String, String> request, HttpSession session) {
        List<String> favBooks = (List<String>) session.getAttribute("favBooks");

        if (favBooks == null) {
            favBooks = new ArrayList<>();
        }

        favBooks.add(request.get("book"));
        session.setAttribute("favBooks", favBooks);
        return "Book added!";
    }
}
