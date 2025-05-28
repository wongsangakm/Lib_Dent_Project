package com.example.project.controller;

import com.example.project.model.Book;
import com.example.project.model.BookFavorite;
import com.example.project.model.User;
import com.example.project.repository.BookRepository;
import com.example.project.repository.FavoriteRepository;
import com.example.project.repository.UserRepository;
import com.example.project.service.EmailService;
import com.example.project.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "https://requestbooks-dentkku.vercel.app")
@RestController
@RequestMapping("/api/auth/favorites")

public class FavoriteController {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    //  เพิ่ม favorite (หากยังไม่เคยกด)
@PostMapping("/{bookId}")
public ResponseEntity<?> addFavorite(@PathVariable Long bookId, @AuthenticationPrincipal User user) {
    if (user == null) return ResponseEntity.status(401).body("Unauthorized");

    Long userId = user.getId();

    if (favoriteRepository.existsByUserIdAndBookId(userId, bookId)) {
        return ResponseEntity.ok(Map.of("message", "Already favorited"));
    }

    Book book = bookRepository.findById(bookId).orElseThrow();
    if (book.getStatus() == null || book.getStatus().isBlank()) {
        book.setStatus("popular_request");
        bookRepository.save(book);
    }

    BookFavorite favorite = new BookFavorite();
    favorite.setUser(user);
    favorite.setBook(book);
    favoriteRepository.save(favorite);

    emailService.sendNotificationToAdmin(book.getBookTitle(), user);

    return ResponseEntity.ok(Map.of("success", true));
}



    // ✅ ตรวจสอบว่า user เคย favorite หรือยัง

@GetMapping
public ResponseEntity<?> getUserFavorites(@AuthenticationPrincipal User user) {
    if (user == null) {
        return ResponseEntity.status(401).body("Unauthorized");
    }

    Long userId = user.getId();
    List<BookFavorite> favs = favoriteRepository.findByUserId(userId);
    List<Book> books = favs.stream().map(BookFavorite::getBook).toList();

    return ResponseEntity.ok(books);
}


    


@GetMapping("/{bookId}")
public ResponseEntity<?> checkFavorite(@PathVariable Long bookId, @AuthenticationPrincipal User user) {
    if (user == null) return ResponseEntity.status(401).body("Unauthorized");

    boolean isFav = favoriteRepository.existsByUserIdAndBookId(user.getId(), bookId);
    return ResponseEntity.ok(Map.of("isFavorited", isFav));
}




    @GetMapping("/popular")
    public ResponseEntity<?> getMostFavoritedBooks() {
        List<Object[]> result = favoriteRepository.countFavoritesByBook();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : result) {
            Long bookId = (Long) row[0];
            Long count = (Long) row[1];

            bookRepository.findById(bookId).ifPresent(book -> {
                Map<String, Object> map = new HashMap<>();
                map.put("book", book);
                map.put("count", count);
                response.add(map);
            });
        }

        return ResponseEntity.ok(response);
    }
    @GetMapping("/admin/favorite-counts")
    public ResponseEntity<?> getFavoriteCountsForAdmin() {
        List<Object[]> raw = favoriteRepository.countFavoritesByBook();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : raw) {
            Map<String, Object> map = new HashMap<>();
            map.put("bookTitle", row[1]);
            map.put("favoriteCount", row[2]);
            result.add(map);
        }
        return ResponseEntity.ok(result);
    }

@GetMapping("/ids")
public ResponseEntity<?> getFavoriteBookIds(@AuthenticationPrincipal User user) {
    if (user == null) return ResponseEntity.status(401).body("Unauthorized");

    List<Long> bookIds = favoriteRepository.findBookIdsByUserId(user.getId());
    return ResponseEntity.ok(bookIds);
}




    @GetMapping("/book/{bookId}/users")
    public ResponseEntity<?> getUsersWhoFavorited(@PathVariable Long bookId) {
        List<BookFavorite> favs = favoriteRepository.findByBookId(bookId);
        List<Map<String, Object>> users = favs.stream()
            .map(fav -> {
                User user = fav.getUser();
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("id", user.getId());
                userMap.put("name", user.getFirstName() + " " + user.getLastName());
                return userMap;
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }



    
}
