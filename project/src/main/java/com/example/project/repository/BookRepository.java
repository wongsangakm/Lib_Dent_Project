package com.example.project.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.project.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn); 
}
