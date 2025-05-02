package com.example.project.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.model.User;
import com.example.project.payload.request.LoginRequest;
import com.example.project.service.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173") // ให้ Vue เข้าถึงได้
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    Optional<User> user = userService.authenticate(request.getUsername(), request.getPassword());
    if (user.isPresent()) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Login success");
        response.put("username", user.get().getUsername());
        response.put("role", user.get().getRole()); // 👈 ส่ง role
        return ResponseEntity.ok(response);
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
}

}
