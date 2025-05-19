package com.example.project.controller;

import com.example.project.model.AdditionalRequest;
import com.example.project.model.User;
import com.example.project.payload.request.AdditionalRequestDTO;
import com.example.project.repository.UserRepository;
import com.example.project.service.AdditionalRequestService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/requests")
public class AdditionalRequestController {

    private final AdditionalRequestService service;
    private final UserRepository userRepository; // ✅ ใช้ instance, ไม่ใช่ชื่อ class

    public AdditionalRequestController(AdditionalRequestService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> createRequest(@Valid @RequestBody AdditionalRequestDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // ✅ ใช้งานตัวแปร userRepository ที่ประกาศไว้
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        String fullName = user.getFirstName() + " " + user.getLastName();

        AdditionalRequest request = new AdditionalRequest();
        request.setBookTitle(dto.getBookTitle());
        request.setAuthor(dto.getAuthor());
        request.setPublisher(dto.getPublisher());
        request.setIsbn(dto.getIsbn());
        request.setYear(dto.getYear());
        request.setPrice(dto.getPrice());
        request.setDescription(dto.getDescription());
        request.setReason(dto.getReason());

        AdditionalRequest saved = service.saveRequest(request, fullName);

        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<AdditionalRequest> getAllRequests() {
        return service.getAllRequests();
    }
}
