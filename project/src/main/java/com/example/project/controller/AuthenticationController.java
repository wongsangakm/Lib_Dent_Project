package com.example.project.controller;

import com.example.project.model.User;
import com.example.project.payload.request.LoginRequest;
import com.example.project.repository.UserRepository;
import com.example.project.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "https://requestbooks-dentkku.vercel.app", allowCredentials = "true")
public class AuthenticationController {

    @Autowired
    private UserService userService;

        @Autowired
    private UserRepository userRepository; 

    @Autowired
    private PasswordEncoder passwordEncoder;
    //  Login regenerate session ใหม่
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        try {
            System.out.println("📱 เข้ามา login request จากมือถือหรืออุปกรณ์ใดก็ตาม");
            System.out.println("Username (raw): [" + request.getUsername() + "]");
            System.out.println("Password (raw): [" + request.getPassword() + "]");

            Enumeration<String> headerNames = httpRequest.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                System.out.println(headerName + ": " + httpRequest.getHeader(headerName));
            }

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

            boolean requireChangePassword = request.getPassword().equals("123456");

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login success");
            response.put("username", user.getUsername());
            response.put("role", user.getRole());
            System.out.println("userId: " + session.getAttribute("userId")); // ✅ เพิ่มไว้หลัง setAttribute
            response.put("requireChangePassword", requireChangePassword);
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
        Long userId = (Long) session.getAttribute("userId");

        if (username == null || role == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }
        Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) return ResponseEntity.status(404).body("User not found");

        User user = userOpt.get();

        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("role", role);
        response.put("firstName", user.getFirstName()); 
        return ResponseEntity.ok(response);
    }

    //  Logout
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body("Unauthorized");

        String newPassword = body.get("newPassword");
        if (newPassword == null || newPassword.length() < 6) {
            return ResponseEntity.badRequest().body("Password too short");
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) return ResponseEntity.status(404).body("User not found");

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword)); // เปลี่ยนรหัส
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Password changed"));

    }



}
