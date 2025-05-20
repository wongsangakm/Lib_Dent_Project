package com.example.project.controller;

import com.example.project.model.AcademicField;
import com.example.project.model.User;
import com.example.project.payload.request.UserRequest;
import com.example.project.repository.AcademicFieldRepository;
import com.example.project.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController 
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private AcademicFieldRepository academicFieldRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;



    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserRequest req) {
        User user = new User();
        user.setUsername(req.username);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setFirstName(req.firstName);
        user.setLastName(req.lastName);
        user.setFirstNameEn(req.firstNameEn);
        user.setLastNameEn(req.lastNameEn);
        user.setEmail(req.email);
        user.setRole(req.role);
        user.setCreatedAt(LocalDateTime.now());

        // ถ้าไม่ใช่ ADMIN ต้องมี branchId
        if (!"ADMIN".equalsIgnoreCase(req.role)) {
            Optional<AcademicField> fieldOpt = academicFieldRepository.findById(req.branchId);
            if (fieldOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "ไม่พบสาขาวิชา"));
            }
            user.setAcademicField(fieldOpt.get());
        }

        userRepository.save(user);
        return ResponseEntity.ok(Map.of("success", true));
    }
}
