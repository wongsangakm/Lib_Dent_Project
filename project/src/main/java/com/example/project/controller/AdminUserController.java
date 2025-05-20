package com.example.project.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.model.User;
import com.example.project.repository.UserRepository;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@RequestMapping("/api")
public class AdminUserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PatchMapping("/admin/users/{id}/change-password")
    public ResponseEntity<?> changePasswordByAdmin(
        @PathVariable Long id,
        @RequestBody Map<String, String> body
    ) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        String newPassword = body.get("newPassword");
        if (newPassword == null || newPassword.length() < 6) {
            return ResponseEntity.badRequest().body("Password too short");
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Password changed"));
    }

    @GetMapping("/admin/users")
    public List<User> getAllUsers() {
        return userRepository.findAll(); // หรือกำหนด field ที่ต้องการส่งกลับ
    }

    @CrossOrigin(
    origins = "http://localhost:5173",
    methods = {RequestMethod.PATCH, RequestMethod.OPTIONS},
    allowedHeaders = "*",
    allowCredentials = "true"
    )
    @PatchMapping("/admin/users/{id}/reset-password")
    public ResponseEntity<?> resetPassword(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) return ResponseEntity.status(404).body("User not found");

        User user = userOpt.get();
        String defaultPassword = "123456";

        user.setPassword(passwordEncoder.encode(defaultPassword));
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Password reset to default"));
    }


}
