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

    public Optional<User> authenticate(String username, String password) {
        return userRepository.findByUsernameIgnoreCase(username)
            .filter(user -> {
                String stored = user.getPassword();
                System.out.println("💬 Stored: " + stored);
                System.out.println("💬 Input: " + password);

                if (stored.startsWith("$2a$")) {
                    boolean match = passwordEncoder.matches(password, stored);
                    System.out.println("✅ Bcrypt match = " + match);
                    return match;
                } else {
                    boolean match = password.equals(stored); // Plaintext
                    System.out.println("✅ Plain match = " + match);
                    return match;
                }
            });
    }
}
