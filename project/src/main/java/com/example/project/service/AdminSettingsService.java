package com.example.project.service;

import com.example.project.model.AdminSettings;
import com.example.project.repository.AdminSettingsRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminSettingsService {

    private final AdminSettingsRepository repository;

    public AdminSettingsService(AdminSettingsRepository repository) {
        this.repository = repository;
    }

    public String getRecipientEmail() {
        return repository.findAll().stream()
                .findFirst()
                .map(AdminSettings::getRecipientEmail)
                .orElse("default@example.com"); // fallback default
    }

    public void updateRecipientEmail(String newEmail) {
        AdminSettings settings = repository.findAll().stream()
                .findFirst()
                .orElse(new AdminSettings());

        settings.setRecipientEmail(newEmail);
        repository.save(settings);
    }
}
