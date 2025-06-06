package com.example.project.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.*;

@Entity
@Data
public class AdditionalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String bookTitle;
    private String author;
    private String publisher;
    private String isbn;
    private Integer year;
    private double price;
    private String rejectReason;


    @Column(length = 1000)
    private String description;

    @Column(length = 1000)
    private String reason;

    private String requestedBy;

    @Column(name = "status")
    private String status = RequestStatus.PENDING;

    @Temporal(TemporalType.TIMESTAMP)
    private Date requestDate;

    public enum FinalApprovalStatus {
    IN_SHELF, ORDERED, POPULAR_REQUEST
    }

}