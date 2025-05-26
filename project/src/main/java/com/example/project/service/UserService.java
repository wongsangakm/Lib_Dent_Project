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
            // ❗ เช็คว่าเป็น plaintext ตรง ๆ
            return password.equals(stored);  
        });
}







}
