package com.example.project.controller;

import com.example.project.payload.request.AdditionalRequestDTO;
import com.example.project.model.AdditionalRequest;
import com.example.project.service.AdditionalRequestService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/admin/request-table")
public class AdminRequestController {

    private final AdditionalRequestService service;

    public AdminRequestController(AdditionalRequestService service) {
        this.service = service;
    }

    @GetMapping
    public List<AdditionalRequestDTO> getAllRequests() {
        return service.getAllRequests().stream().map(AdditionalRequestDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdditionalRequestDTO> getRequestById(@PathVariable Long id) {
        AdditionalRequest request = service.getRequestById(id);
        return ResponseEntity.ok(new AdditionalRequestDTO(request));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approveRequest(@PathVariable Long id) {
        service.approveRequest(id);
        return ResponseEntity.ok("Request approved");
    }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<String> deleteRequest(@PathVariable Long id) {
    //     service.deleteRequest(id);
    //     return ResponseEntity.ok("Request deleted");
    // }

    @PutMapping("/{id}/reject")
    public ResponseEntity<String> rejectRequest(@PathVariable Long id) {
    service.rejectRequest(id);
    return ResponseEntity.ok("Request rejected");
    }

}
