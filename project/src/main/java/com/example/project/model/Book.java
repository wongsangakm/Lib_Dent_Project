package com.example.project.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookTitle;
    private String isbn;
    private String url;
    private String author;
    private String edition;
    private String year;
    private Integer quantity;
    private Double price;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; 

    @Column(nullable = true)
    private String status;

    @Column(name = "cover_image")
    private String coverImage;

    @Column(name = "publisher")
    private String publisher;

    /**
     *
     */
    @Column(columnDefinition = "TEXT") // หรือ VARCHAR(4000) แล้วแต่ฐานข้อมูล
    private String description;


    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<BookFavorite> favorites = new ArrayList<>();



}
