package com.example.project.controller;

import com.example.project.model.User;
import com.example.project.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController 
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode("123456"));
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("success", true));
    }
}
