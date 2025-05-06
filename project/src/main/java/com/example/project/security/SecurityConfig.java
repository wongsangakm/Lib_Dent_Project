package com.example.project.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests() // แทน authorizeRequests()
            .requestMatchers(
                "/", 
                "/api/books",
                "/api/book/**",
                "/images/**",
                "/css/**",
                "/js/**",
                "/api/auth/**"
            ).permitAll()
            .anyRequest().authenticated()
            .and()
            .httpBasic();
        return http.build();
    }
}

