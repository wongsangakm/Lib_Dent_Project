package com.example.project.service;

import org.springframework.stereotype.Service;

import com.example.project.model.AdditionalRequest;
import com.example.project.repository.AdditionalRequestRepository;

import java.util.List;

@Service
public class AdditionalRequestService {

    private final AdditionalRequestRepository repository;
    private final EmailService emailService;

    public AdditionalRequestService(AdditionalRequestRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    public AdditionalRequest saveRequest(AdditionalRequest request, String fullName) {
        AdditionalRequest saved = repository.save(request);
        emailService.sendAdditionalRequestNotification(saved, fullName);
        return saved;
    }

    public List<AdditionalRequest> getAllRequests() {
        return repository.findAll();
    }
}
