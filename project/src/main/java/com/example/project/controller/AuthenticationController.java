package com.example.project.controller;

import com.example.project.model.User;
import com.example.project.payload.request.LoginRequest;
import com.example.project.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    //  Login regenerate session ใหม่
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        try {
            Optional<User> userOpt = userService.authenticate(request.getUsername(), request.getPassword());
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            //  ลบ session เก่าและสร้างใหม่
            HttpSession oldSession = httpRequest.getSession(false);
            if (oldSession != null) oldSession.invalidate();

            HttpSession session = httpRequest.getSession(true); // regenerate session
            User user = userOpt.get();

            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());

            System.out.println("✅ Login success: " + user.getUsername()); 

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login success");
            response.put("username", user.getUsername());
            response.put("role", user.getRole());
            System.out.println("userId: " + session.getAttribute("userId")); // ✅ เพิ่มไว้หลัง setAttribute

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("เกิดข้อผิดพลาดในระบบ: " + e.getMessage());
        }
    }

    //  ตรวจสอบ user ที่ login อยู่
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");

        if (username == null || role == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("role", role);

        return ResponseEntity.ok(response);
    }

    //  Logout
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully");
    }
}
