package com.example.project.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role;
    @ElementCollection
    private List<Long> favBooks;
    @Column(nullable = true)
    private String firstName;

    @Column(nullable = true)
    private String lastName;
    
    @Column(name = "first_name_en")
    private String firstNameEn;

    @Column(name = "last_name_en")
    private String lastNameEn;

    @Column(nullable = true)
    private String email;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}

