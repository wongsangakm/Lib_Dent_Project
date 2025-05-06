package com.example.project.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.model.Book;
import com.example.project.model.User;
import com.example.project.model.UserAction;
import com.example.project.repository.BookRepository;
import com.example.project.repository.UserActionRepository;

import java.util.Optional;

@Service
public class UserActionService {

    @Autowired
    private UserActionRepository userActionRepository;

    @Autowired
    private BookRepository bookRepository;

    // บันทึกการกระทำ
    public void recordAction(User user, Book book, String actionType) {
        UserAction action = new UserAction();
        action.setUser(user);
        action.setBook(book);
        action.setActionType(actionType);
        userActionRepository.save(action);
    }

    // ดึงรายการโปรดของผู้ใช้
    public List<Book> getFavouritesByUser(User user) {
        List<UserAction> actions = userActionRepository.findByUserAndActionType(user, "FAVORITE");
        return actions.stream().map(UserAction::getBook).collect(Collectors.toList());
    }

    // ลบการกระทำ
    public void removeAction(User user, Book book, String actionType) {
        Optional<UserAction> actionOptional = userActionRepository.findByUserAndBookAndActionType(user, book, actionType);
        
        // ใช้ .orElseThrow() เพื่อโยน exception ถ้าไม่พบ
        UserAction action = actionOptional.orElseThrow(() -> new RuntimeException("Action not found"));

        userActionRepository.delete(action);
    }
}
