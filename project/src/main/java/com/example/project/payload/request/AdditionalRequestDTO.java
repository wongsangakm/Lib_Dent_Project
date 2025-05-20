package com.example.project.payload.request;

import com.example.project.model.AdditionalRequest;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class AdditionalRequestDTO {
    private Long id;

    @NotBlank private String bookTitle;
    @NotBlank private String author;
    @NotBlank private String publisher;
    @NotNull private Integer isbn;
    @NotNull private Integer year;
    @NotNull private Double price;
    private String description;
    @NotBlank private String reason;
    private String requestedBy;
    private Date requestDate;
    private String status;

    public AdditionalRequestDTO(AdditionalRequest request) {
        this.id = request.getId();
        this.bookTitle = request.getBookTitle();
        this.author = request.getAuthor();
        this.publisher = request.getPublisher();
        this.isbn = request.getIsbn();
        this.year = request.getYear();
        this.price = request.getPrice();
        this.description = request.getDescription();
        this.reason = request.getReason();
        this.requestedBy = request.getRequestedBy();
        this.status = request.getStatus();
        this.requestDate = request.getRequestDate();
    }
}