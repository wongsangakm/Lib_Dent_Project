package com.example.project.service;

import org.springframework.stereotype.Service;

import com.example.project.model.AdditionalRequest;
import com.example.project.repository.AdditionalRequestRepository;

import java.util.List;

@Service
public class AdditionalRequestService {

    private final AdditionalRequestRepository repository;
    private final EmailService emailService;

    // Constructor Injection (ไม่มี @Autowired เพราะมี constructor เดียว)
    public AdditionalRequestService(AdditionalRequestRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    // บันทึกคำขอและส่งอีเมลแจ้งเตือน
    public AdditionalRequest saveRequest(AdditionalRequest request, String fullName) {
        request.setRequestedBy(fullName);
        request.setStatus("PENDING");
        AdditionalRequest saved = repository.save(request);
        emailService.sendAdditionalRequestNotification(saved, fullName);
        return saved;
    }

    // ดึงข้อมูลคำขอทั้งหมด
    public List<AdditionalRequest> getAllRequests() {
        return repository.findAll();
    }
}
