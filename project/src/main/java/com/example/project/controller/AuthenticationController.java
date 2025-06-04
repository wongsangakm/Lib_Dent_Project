package com.example.project.controller;

import com.example.project.model.User;
import com.example.project.payload.request.LoginRequest;
import com.example.project.repository.BookRepository;
import com.example.project.repository.UserRepository;
import com.example.project.service.UserService;
import com.example.project.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {
    "http://localhost:5173",
    "https://requestbooks-dentkku.vercel.app"
}, allowCredentials = "true")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        Optional<User> userOpt = userService.authenticate(request.getUsername(), request.getPassword());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        User user = userOpt.get();
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        // ใส่ token ลง header (optional)
        response.setHeader("Authorization", "Bearer " + token);

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Login success");
        body.put("username", user.getUsername());
        body.put("role", user.getRole());
        body.put("token", token);
        body.put("requireChangePassword", request.getPassword().equals("123456")); // เงื่อนไขเปลี่ยนรหัส

        return ResponseEntity.ok(body);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
        }

        String token = authHeader.substring(7);
        try {
            Claims claims = jwtUtil.validateToken(token);
            String username = claims.getSubject();
            String role = (String) claims.get("role");

            Optional<User> userOpt = userRepository.findByUsernameIgnoreCase(username);
            if (userOpt.isEmpty()) return ResponseEntity.status(404).body("User not found");

            User user = userOpt.get();
            Map<String, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("role", role);
            response.put("firstName", user.getFirstName());
            response.put("userId", user.getId());

            return ResponseEntity.ok(response);
        } catch (ExpiredJwtException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body, HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        try {
            String jwt = authHeader.substring(7);
            Claims claims = jwtUtil.validateToken(jwt);
            String username = claims.getSubject();

            Optional<User> userOpt = userRepository.findByUsernameIgnoreCase(username);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(404).body("User not found");
            }

            String newPassword = body.get("newPassword");
            if (newPassword == null || newPassword.length() < 6) {
                return ResponseEntity.badRequest().body("Password too short");
            }

            User user = userOpt.get();
            user.setPassword(newPassword); // 👈 อย่าลืมเข้ารหัสถ้าใช้ bcrypt
            userRepository.save(user);

            return ResponseEntity.ok(Map.of("message", "Password changed"));

        } catch (ExpiredJwtException ex) {
            return ResponseEntity.status(401).body("Token expired");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }
}
