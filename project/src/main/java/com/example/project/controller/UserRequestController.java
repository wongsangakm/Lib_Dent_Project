package com.example.project.controller;

import com.example.project.payload.request.AdditionalRequestDTO;
import com.example.project.model.AdditionalRequest;
import com.example.project.model.User;
import com.example.project.repository.UserRepository;
import com.example.project.service.AdditionalRequestService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Date;

@CrossOrigin(origins = "https://requestbooks-dentkku.vercel.app")
@RestController
@RequestMapping("/api/requests")
public class UserRequestController {

    private final AdditionalRequestService service;
    private final UserRepository userRepository;

    public UserRequestController(AdditionalRequestService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<AdditionalRequest> createRequest(@Valid @RequestBody AdditionalRequestDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsernameIgnoreCase(username)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated"));

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
        request.setRequestedBy(fullName);
        request.setStatus("Pending");
        request.setRequestDate(new Date());

        AdditionalRequest saved = service.saveRequest(request, fullName);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdditionalRequestDTO> getRequestById(@PathVariable Long id) {
    AdditionalRequest request = service.getRequestById(id);
    return ResponseEntity.ok(new AdditionalRequestDTO(request));
    }



    @GetMapping
    public List<AdditionalRequestDTO> getAllRequests() {
        return service.getAllRequests().stream().map(AdditionalRequestDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/history")
    public List<AdditionalRequestDTO> getRequestHistory() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();

    User user = userRepository.findByUsernameIgnoreCase(username)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated"));

    String fullName = user.getFirstName() + " " + user.getLastName();

    return service.getRequestsByRequester(fullName).stream()
        .map(AdditionalRequestDTO::new)
        .collect(Collectors.toList());
    }


}
