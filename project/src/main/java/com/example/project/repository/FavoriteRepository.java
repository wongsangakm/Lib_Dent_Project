package com.example.project.repository;

import com.example.project.model.BookFavorite;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<BookFavorite, Long> {
    boolean existsByUserIdAndBookId(Long userId, Long bookId);

    List<BookFavorite> findByUserId(Long id);
}
