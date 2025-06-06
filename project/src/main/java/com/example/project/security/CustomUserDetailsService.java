package com.example.project.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.project.model.User;
import com.example.project.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("🔍 login username: " + username); 
        User user = userRepository.findByUsernameIgnoreCase(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

             System.out.println("✅ user found: " + user.getUsername());
    System.out.println("🔐 password hash: " + user.getPassword());
    System.out.println("👤 role: " + user.getRole());
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            List.of(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}
