package com.example.project.repository;

import com.example.project.model.BookFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<BookFavorite, Long> {
    boolean existsByUserIdAndBookId(Long userId, Long bookId);
}
