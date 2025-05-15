package com.example.project.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
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
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    @Column(nullable = true)
    private String status;

    /**
     *
     */
    @Column(name = "cover_image")
    private String coverImage;

    @Column(name = "publisher")
    private String publisher;

    /**
     *
     */
    @Column(columnDefinition = "LONGTEXT") // หรือ VARCHAR(4000) แล้วแต่ฐานข้อมูล
    private String description;
    

    public String getPublisher() {
        return publisher;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCoverImage() {
        return coverImage;
    }
    
    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getBookTitle() {
        return bookTitle;
    }
    
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getEdition() { return edition; }
    public void setEdition(String edition) { this.edition = edition; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

}
