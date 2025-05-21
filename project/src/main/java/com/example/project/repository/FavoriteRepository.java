package com.example.project.repository;

import com.example.project.model.BookFavorite;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavoriteRepository extends JpaRepository<BookFavorite, Long> {
    boolean existsByUserIdAndBookId(Long userId, Long bookId);
    @Query("SELECT f.book.id FROM BookFavorite f WHERE f.user.id = :userId")
    List<Long> findBookIdsByUserId(@Param("userId") Long userId);
    List<BookFavorite> findByUserId(Long userId);

    List<BookFavorite> findByBookId(Long bookId);
    @Query("""
    SELECT f.book.id, f.book.bookTitle, COUNT(f)
    FROM BookFavorite f
    GROUP BY f.book.id, f.book.bookTitle
    HAVING COUNT(f) > 0
    """)
    List<Object[]> countFavoritesByBook();
    List<BookFavorite> findByCreatedAtBefore(LocalDateTime cutoff);

    @Query("SELECT f FROM BookFavorite f WHERE f.createdAt <= :cutoff")
    List<BookFavorite> findAllBefore(@Param("cutoff") LocalDateTime cutoff);


    @Query("""
    SELECT u.academicField.nameTh, COUNT(DISTINCT u.id)
    FROM BookFavorite f
    JOIN f.user u
    WHERE u.academicField IS NOT NULL
    GROUP BY u.academicField.nameTh
""")
List<Object[]> countUsersByAcademicField();

@Query("SELECT DISTINCT bf.book.id FROM BookFavorite bf")
List<Long> findBookIdsWithFavorites();

long countByBookId(Long bookId);

}
