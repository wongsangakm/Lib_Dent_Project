package com.example.project.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.project.model.Book;
import com.example.project.model.User;
import com.example.project.repository.BookRepository;
import com.example.project.repository.UserRepository;

@Service
public class UserActionService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BookRepository bookRepository;

    @Transactional
    public void recordAction(User user, Book book) {
        user.getFavBooks().add(book.getId());
        userRepository.save(user);
    }

    @Transactional
    public void removeAction(User user, Book book) {
        user.getFavBooks().remove(book.getId());
        userRepository.save(user);
    }

    public List<Book> getFavouritesByUser(User user) {
        return user.getFavBooks().stream()
                .map(bookRepository::findById) // คืนค่า Optional<Book>
                .flatMap(Optional::stream) // แปลง Optional<Book> เป็น Book
                .collect(Collectors.toList());
    }
}
