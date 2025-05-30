package com.example.project.repository;

import com.example.project.model.UserAction;
import com.example.project.model.User;
import com.example.project.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserActionRepository extends JpaRepository<UserAction, Long> {
    List<UserAction> findByUser(User user);
    List<UserAction> findByBook(Book book);
    boolean existsByUserAndBookAndActionType(User user, Book book, String actionType);
    List<UserAction> findByUserAndActionType(User user, String string);
    Optional<UserAction> findByUserAndBookAndActionType(User user, Book book, String actionType);

}
