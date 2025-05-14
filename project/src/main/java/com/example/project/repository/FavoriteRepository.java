package com.example.project.repository;

import com.example.project.model.BookFavorite;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavoriteRepository extends JpaRepository<BookFavorite, Long> {
    boolean existsByUserIdAndBookId(Long userId, Long bookId);
    @Query("SELECT f.book.id FROM BookFavorite f WHERE f.user.id = :userId")
    List<Long> findBookIdsByUserId(@Param("userId") Long userId);
    List<BookFavorite> findByUserId(Long userId);
}
