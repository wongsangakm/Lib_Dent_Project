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
    Optional<User> userOpt = userRepository.findByUsernameIgnoreCase(username);
    
    if (userOpt.isEmpty()) {
        System.out.println("❌ User not found: " + username);
        return Optional.empty();
    }

    User user = userOpt.get();
    String storedPassword = user.getPassword();
    
    if (storedPassword == null) {
        System.out.println("❌ Stored password is null for user: " + username);
        return Optional.empty();
    }

    boolean match = passwordEncoder.matches(rawPassword, storedPassword);
    System.out.println("✅ Password match result: " + match);
    
    return match ? userOpt : Optional.empty();
}





}
