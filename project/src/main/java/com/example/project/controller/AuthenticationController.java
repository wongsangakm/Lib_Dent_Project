package com.example.project.controller;

import com.example.project.model.User;
import com.example.project.payload.request.LoginRequest;
import com.example.project.repository.UserRepository;
import com.example.project.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
@CrossOrigin(
    origins = "https://requestbooks-dentkku.vercel.app", 
    allowCredentials = "true",
    allowedHeaders = "*",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository; 

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, 
                                  HttpServletRequest httpRequest, 
                                  HttpServletResponse httpResponse) {
        try {
            System.out.println("📱 Login request from: " + httpRequest.getHeader("user-agent"));
            System.out.println("Username: [" + request.getUsername() + "]");
            System.out.println("Password: [" + request.getPassword() + "]");

            // Log existing cookies
            logCookies(httpRequest, "Login");

            Optional<User> userOpt = userService.authenticate(request.getUsername(), request.getPassword());
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }

            // ลบ session เก่า
            HttpSession oldSession = httpRequest.getSession(false);
            if (oldSession != null) {
                System.out.println("🗑️ Invalidating old session: " + oldSession.getId());
                oldSession.invalidate();
            }

            // สร้าง session ใหม่
            HttpSession session = httpRequest.getSession(true);
            User user = userOpt.get();

            // เก็บข้อมูล user ใน session
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole().toString());
            session.setAttribute("isAuthenticated", true);

            // ตั้งค่า session timeout
            session.setMaxInactiveInterval(3600); // 1 ชั่วโมง

            // สำคัญ: ตั้งค่า Response Headers ก่อนส่ง Cookie
            setupCorsHeaders(httpResponse);
            
            // ส่ง Cookie เพิ่มเติมสำหรับ iOS Safari
            setupSessionCookie(httpResponse, session.getId());

            System.out.println("✅ Login success: " + user.getUsername());
            System.out.println("🍪 Session ID: " + session.getId());
            System.out.println("👤 User ID: " + user.getId());

            boolean requireChangePassword = request.getPassword().equals("123456");

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "Login success");
            responseBody.put("username", user.getUsername());
            responseBody.put("role", user.getRole().toString());
            responseBody.put("requireChangePassword", requireChangePassword);
            responseBody.put("sessionId", session.getId()); // ส่ง session ID ไปด้วย
            
            return ResponseEntity.ok(responseBody);

        } catch (Exception e) {
            System.err.println("❌ Login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body("เกิดข้อผิดพลาดในระบบ: " + e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session, 
                                          HttpServletRequest httpRequest,
                                          HttpServletResponse httpResponse) {
        
        // ตั้งค่า CORS headers
        setupCorsHeaders(httpResponse);
        
        // Log cookies
        logCookies(httpRequest, "/me");
        
        System.out.println("🔍 Session ID: " + (session != null ? session.getId() : "null"));
        System.out.println("🔍 Session New: " + (session != null ? session.isNew() : "null"));
        
        if (session == null) {
            System.out.println("❌ No session found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No session");
        }

        // ตรวจสอบข้อมูลใน session
        Object userIdObj = session.getAttribute("userId");
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        Boolean isAuthenticated = (Boolean) session.getAttribute("isAuthenticated");

        System.out.println("📋 Session data - UserId: " + userIdObj + ", Username: " + username + ", Role: " + role + ", Auth: " + isAuthenticated);
        
        if (username == null || role == null || !Boolean.TRUE.equals(isAuthenticated)) {
            System.out.println("❌ Invalid session data");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }

        Long userId;
        try {
            userId = (Long) userIdObj;
        } catch (ClassCastException e) {
            System.out.println("❌ Invalid userId type: " + userIdObj);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid session");
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            System.out.println("❌ User not found: " + userId);
            return ResponseEntity.status(404).body("User not found");
        }

        User user = userOpt.get();

        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("role", role);
        response.put("firstName", user.getFirstName());
        response.put("sessionId", session.getId());
        
        System.out.println("✅ User authenticated: " + username);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session, HttpServletResponse httpResponse) {
        setupCorsHeaders(httpResponse);
        
        if (session != null) {
            System.out.println("🚪 Logging out session: " + session.getId());
            session.invalidate();
            
            // ลบ cookie
            Cookie cookie = new Cookie("JSESSIONID", "");
            cookie.setMaxAge(0);
            cookie.setPath("/");
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setAttribute("SameSite", "None");
            httpResponse.addCookie(cookie);
        }
        
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> body, 
                                          HttpSession session,
                                          HttpServletResponse httpResponse) {
        setupCorsHeaders(httpResponse);
        
        if (session == null) {
            return ResponseEntity.status(401).body("No session");
        }
        
        Long userId = (Long) session.getAttribute("userId");
        Boolean isAuthenticated = (Boolean) session.getAttribute("isAuthenticated");
        
        if (userId == null || !Boolean.TRUE.equals(isAuthenticated)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String newPassword = body.get("newPassword");
        if (newPassword == null || newPassword.length() < 6) {
            return ResponseEntity.badRequest().body("Password too short");
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("message", "Password changed"));
    }

    // Helper methods
    private void setupCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", "https://requestbooks-dentkku.vercel.app");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, X-Requested-With");
        response.setHeader("Access-Control-Max-Age", "3600");
        
        // สำหรับ iOS Safari
        response.setHeader("Vary", "Origin, Access-Control-Request-Method, Access-Control-Request-Headers");
    }

    private void setupSessionCookie(HttpServletResponse response, String sessionId) {
        // Cookie หลัก
        Cookie sessionCookie = new Cookie("JSESSIONID", sessionId);
        sessionCookie.setSecure(true);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(3600); // 1 ชั่วโมง
        sessionCookie.setAttribute("SameSite", "None");
        response.addCookie(sessionCookie);

        // Cookie สำรอง (สำหรับ iOS Safari ที่บางครั้งมีปัญหา)
        Cookie backupCookie = new Cookie("SESSION_BACKUP", sessionId);
        backupCookie.setSecure(true);
        backupCookie.setHttpOnly(false); // ให้ JavaScript อ่านได้
        backupCookie.setPath("/");
        backupCookie.setMaxAge(3600);
        backupCookie.setAttribute("SameSite", "None");
        response.addCookie(backupCookie);
        
        System.out.println("🍪 Set cookies - Main: JSESSIONID, Backup: SESSION_BACKUP");
    }

    private void logCookies(HttpServletRequest request, String endpoint) {
        System.out.println("🍪 === Cookies for " + endpoint + " ===");
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                System.out.println("🍪 " + cookie.getName() + " = " + cookie.getValue());
            }
        } else {
            System.out.println("🍪 No cookies found");
        }
        System.out.println("🍪 =====================================");
    }
}