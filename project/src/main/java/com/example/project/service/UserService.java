package com.example.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.example.project.model.User;
import com.example.project.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

public Optional<User> authenticate(String username, String rawPassword) {
    System.out.println("🔍 Looking for username: " + username);

    Optional<User> userOpt = userRepository.findByUsernameIgnoreCase(username);
    if (userOpt.isPresent()) {
        User user = userOpt.get();
        System.out.println("✅ User found. Encoded password = " + user.getPassword());

        boolean matches = passwordEncoder.matches(rawPassword, user.getPassword());
        System.out.println("🔐 Password match: " + matches);
        return matches ? userOpt : Optional.empty();
    }

    System.out.println("❌ User not found");
    return Optional.empty();
}






}
