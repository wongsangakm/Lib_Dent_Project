package com.example.project.repository;

import com.example.project.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByIsbn(String isbn); 
    
    // ค้นหาหนังสือโดยชื่อเรื่อง
    List<Book> findByTitleContainingIgnoreCase(String title);

    // ค้นหาหนังสือโดยผู้เขียน
    List<Book> findByAuthorContainingIgnoreCase(String author);

    // ค้นหาหนังสือทั้งหมด
    @SuppressWarnings("null")
    List<Book> findAll();

    // ค้นหาหนังสือโดย ID
    @SuppressWarnings("null")
    Optional<Book> findById(Long id);
}
