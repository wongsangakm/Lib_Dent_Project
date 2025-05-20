package com.example.project.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdditionalRequestDTO {

    @NotBlank
    private String bookTitle;

    @NotBlank
    private String author;

    @NotBlank
    private String publisher;

    @NotNull
    private Integer isbn;

    @NotNull
    private Integer year;

    @NotNull
    private double price;

    private String description;

    @NotBlank
    private String reason;
}