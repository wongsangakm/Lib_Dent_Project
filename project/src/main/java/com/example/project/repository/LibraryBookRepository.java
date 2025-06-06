package com.example.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.project.model.LibraryBook;

public interface LibraryBookRepository extends JpaRepository<LibraryBook, Long> {
     boolean existsByTitleIgnoreCaseAndAuthorIgnoreCaseAndLangIgnoreCaseAndPubbeyearAndCopy(
        String title, String author, String lang, String pubbeyear, String copy
    );

    List<LibraryBook> findTop5ByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(String title, String author);

    List<LibraryBook> findTop100ByTitleContainingIgnoreCase(String title);


}

