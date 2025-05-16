package com.example.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth/session")
public class SessionController {

    @GetMapping("/check")
    public String checkSession(HttpSession session) {
        String sessionId = session.getId();
        return "Session ID: " + sessionId;
    }
}
