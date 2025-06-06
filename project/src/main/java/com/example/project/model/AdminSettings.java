package com.example.project.model;

import jakarta.persistence.*;

@Entity
public class AdminSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String recipientEmail;

    // getter-setter
    public Long getId() { return id; }

    public String getRecipientEmail() { return recipientEmail; }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }
}
