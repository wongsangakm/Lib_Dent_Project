package com.example.project.controller;

import com.example.project.model.AdditionalRequest;
import com.example.project.model.User;
import com.example.project.payload.request.AdditionalRequestDTO;
import com.example.project.repository.AdditionalRequestRepository;
import com.example.project.repository.UserRepository;
import com.example.project.service.AdditionalRequestService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/admin/request-table")
public class AdditionalRequestController {

    private final AdditionalRequestService service;
    private final UserRepository userRepository;
    private final AdditionalRequestRepository additionalRequestRepository;
    public AdditionalRequestController(
            AdditionalRequestService service,
            UserRepository userRepository,
            AdditionalRequestRepository additionalRequestRepository
    ) {
        this.service = service;
        this.userRepository = userRepository;
        this.additionalRequestRepository = additionalRequestRepository;
    }

    @PostMapping
    public ResponseEntity<AdditionalRequest> createRequest(@Valid @RequestBody AdditionalRequestDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username)
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

        AdditionalRequest saved = service.saveRequest(request, fullName);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<AdditionalRequestDTO> getAllRequests() {
        return additionalRequestRepository.findAll().stream()
                .map(AdditionalRequestDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
public ResponseEntity<AdditionalRequestDTO> getRequestById(@PathVariable Long id) {
    AdditionalRequest request = additionalRequestRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));
    return ResponseEntity.ok(new AdditionalRequestDTO(request));
}

@PutMapping("/{id}/approve")
public ResponseEntity<String> approveRequest(@PathVariable Long id) {
    AdditionalRequest request = additionalRequestRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));

    request.setStatus("Approved"); 
    additionalRequestRepository.save(request);

    return ResponseEntity.ok("Request approved");
}

@DeleteMapping("/{id}")
public ResponseEntity<String> deleteRequest(@PathVariable Long id) {
    if (!additionalRequestRepository.existsById(id)) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found");
    }

    additionalRequestRepository.deleteById(id);
    return ResponseEntity.ok("Request deleted");
}

}
