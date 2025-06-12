package com.example.project.payload.request;

import java.time.LocalDateTime;
import java.util.List;

public class BudgetReportDTO {
    private String title;
    private String isbn;
    private Double price;
    private String publisher;
    private String status;
    private int totalBooks;
    private double totalBudget;
    private List<BookItem> books;
    private List<BookItem> additionalRequests;

    public BudgetReportDTO() {
    }

    public BudgetReportDTO(String title, String isbn, Double price, String publisher, String status) {
        this.title = title;
        this.isbn = isbn;
        this.price = price;
        this.publisher = publisher;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(int totalBooks) {
        this.totalBooks = totalBooks;
    }

    public double getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(double totalBudget) {
        this.totalBudget = totalBudget;
    }

    public List<BookItem> getBooks() {
        return books;
    }

    public void setBooks(List<BookItem> books) {
        this.books = books;
    }

    public List<BookItem> getAdditionalRequests() {
        return additionalRequests;
    }

    public void setAdditionalRequests(List<BookItem> additionalRequests) {
        this.additionalRequests = additionalRequests;
    }

    public static class BookItem {
        private String title;
        private String isbn;
        private Double price;
        private String publisher;
        private String year;
        private String edition;
        private String status;
        private LocalDateTime createdAt;

        // getter / setter

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIsbn() {
            return isbn;
        }

        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getEdition() {
            return edition;
        }

        public void setEdition(String edition) {
            this.edition = edition;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDateTime getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
        }
    }
}
