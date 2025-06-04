package com.example.project.controller;

import com.example.project.payload.request.AdditionalRequestDTO;
import com.example.project.model.AdditionalRequest;
import com.example.project.service.AdditionalRequestService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = {
    "https://requestbooks-dentkku.vercel.app",
    "http://localhost:5173"
}, allowCredentials = "true")
@RestController
@RequestMapping("/api/admin/request-table")
public class AdminRequestController {

    private final AdditionalRequestService service;

    public AdminRequestController(AdditionalRequestService service) {
        this.service = service;
    }

    @GetMapping
    public List<AdditionalRequestDTO> getAllRequests() {
        return service.getAllRequests().stream()
                .map(AdditionalRequestDTO::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdditionalRequestDTO> getRequestById(@PathVariable Long id) {
        AdditionalRequest request = service.getRequestById(id);
        return ResponseEntity.ok(new AdditionalRequestDTO(request));
    }

    // ✅ ยืนยันสถานะพร้อม finalStatus
    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approveRequest(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String finalStatus = body.get("finalStatus"); // เช่น "in_shelf", "ordered", ...
        service.approveRequest(id, finalStatus); // ส่งไปที่ service
        return ResponseEntity.ok("Approved with status: " + finalStatus);
    }

    // ❌ ยกเลิกคำขอ
    @PutMapping("/{id}/reject")
    public ResponseEntity<String> rejectRequest(@PathVariable Long id, @RequestBody Map<String, String> body) {
    String reason = body.get("reason");
    service.rejectRequest(id, reason);
    return ResponseEntity.ok("Request rejected with reason: " + reason);
}

}
