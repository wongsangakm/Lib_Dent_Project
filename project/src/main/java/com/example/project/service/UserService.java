package com.example.project.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.repository.UserRepository;
import com.example.project.model.User;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> authenticate(String username, String password) {
        return userRepository.findByUsername(username)
            .filter(user -> user.getPassword().equals(password)); // ยังไม่เข้ารหัส
    }
}

