package com.example.project.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class AdditionalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookTitle;
    private String author;
    private String publisher;
    private Integer isbn;
    private Integer year;
    private double price;
    
    @Column(length = 1000)
    private String description;

    @Column(length = 1000)
    private String reason;
    
}
