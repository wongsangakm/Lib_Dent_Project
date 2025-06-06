package com.example.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.model.LibraryBook;

public interface LibraryBookRepository extends JpaRepository<LibraryBook, Long> {
     boolean existsByTitleIgnoreCaseAndAuthorIgnoreCaseAndLangIgnoreCaseAndPubbeyearAndCopy(
        String title, String author, String lang, String pubbeyear, String copy
    );

    List<LibraryBook> findTop5ByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(String title, String author);

    List<LibraryBook> findTop100ByTitleContainingIgnoreCase(String title);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM library_book", nativeQuery = true)
    void deleteAllBooksNative();

}

