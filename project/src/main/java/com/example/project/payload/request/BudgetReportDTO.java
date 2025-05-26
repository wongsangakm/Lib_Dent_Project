package com.example.project.payload.request;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class BudgetReportDTO {
    private double totalBudget;
    private int totalBooks;
    private List<BookItem> books;

    // getters & setters

    @Data
    public static class BookItem {
        private String title;
        private String isbn;
        private Double price;
        private String publisher;
        private String year;
        private String edition;
        private String status;
        private LocalDateTime createdAt;

        // getters & setters
    }
}
