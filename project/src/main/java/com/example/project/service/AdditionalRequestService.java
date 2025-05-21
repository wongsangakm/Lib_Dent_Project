package com.example.project.service;

import org.springframework.stereotype.Service;
import com.example.project.model.AdditionalRequest;
import com.example.project.model.RequestStatus;
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

    public List<AdditionalRequest> getRequestsByRequester(String fullName) {
    return repository.findByRequestedBy(fullName);
    }
    

    public AdditionalRequest saveRequest(AdditionalRequest request, String fullName) {
        request.setRequestedBy(fullName);
        request.setStatus(RequestStatus.PENDING);
        AdditionalRequest saved = repository.save(request);
        emailService.sendAdditionalRequestNotification(saved, fullName);
        return saved;
    }

    public List<AdditionalRequest> getAllRequests() {
        return repository.findAll();
    }

    public AdditionalRequest approveRequest(Long id) {
        AdditionalRequest request = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus(RequestStatus.APPROVED);
        return repository.save(request);
    }

    public void deleteRequest(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Request not found");
        }
        repository.deleteById(id);
    }

    public AdditionalRequest getRequestById(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Request not found"));
    }
}