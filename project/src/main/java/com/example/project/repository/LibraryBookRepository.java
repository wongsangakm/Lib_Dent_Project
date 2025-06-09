package com.example.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.project.model.LibraryBook;

public interface LibraryBookRepository extends JpaRepository<LibraryBook, Long> {
    @Modifying
    @Query(value = "TRUNCATE TABLE library_book", nativeQuery = true)
    void truncateTable();
     boolean existsByTitleIgnoreCaseAndAuthorIgnoreCaseAndLangIgnoreCaseAndPubbeyearAndCopy(
        String title, String author, String lang, String pubbeyear, String copy
    );

    List<LibraryBook> findTop5ByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(String title, String author);
    
    List<LibraryBook> findTop100ByTitleContainingIgnoreCase(String title);

}

