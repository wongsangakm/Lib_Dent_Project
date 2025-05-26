package com.example.project.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.model.Book;
import com.example.project.model.BookFavorite;
import com.example.project.payload.request.BudgetReportDTO;
import com.example.project.repository.FavoriteRepository;

@Service
public class BudgetService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    private String normalizeStatus(String status) {
    if (status == null) return null;
    return switch (status.trim()) {
        case "กำลังสั่งซื้อ" -> "pending";
        case "สั่งซื้อแล้ว" -> "ordered";
        case "pending", "ordered" -> status; // ค่าอังกฤษที่ถูกส่งตรง
        default -> status.toLowerCase(); // fallback
    };
}


    public BudgetReportDTO generateReport(Integer year, Integer term, Integer month, String status) {
    List<BookFavorite> favorites = favoriteRepository.findAll();

    System.out.println("📥 เริ่มรายงาน (จาก BookFavorite)");
    System.out.println("🔧 year=" + year + ", term=" + term + ", month=" + month + ", status=" + status);
    System.out.println("⭐ รายการ Favorite ทั้งหมด: " + favorites.size());

    // คำนวณช่วงปีงบประมาณ
    List<BookFavorite> filtered = favorites.stream()
        .filter(fav -> {
            LocalDateTime created = fav.getCreatedAt();
            if (created == null) return false;

            int budgetYear = calculateBudgetYear(created);
            return budgetYear == year;
        })
        .toList();

    System.out.println("✅ หลังกรองตามปีงบประมาณ: " + filtered.size());

    // กรองตาม term
    if (term != null) {
        Set<Integer> months = switch (term) {
            case 1 -> Set.of(5, 6, 7, 8, 9);               // เทอม 1
            case 2 -> Set.of(11, 12, 1, 2, 3);             // เทอม 2
            case 3 -> Set.of(5, 6, 7, 8, 9, 11, 12, 1, 2, 3); // รวมเทอม
            default -> throw new IllegalArgumentException("Invalid term: " + term);
        };

        filtered = filtered.stream()
            .filter(fav -> months.contains(fav.getCreatedAt().getMonthValue()))
            .toList();

        System.out.println("📆 หลังกรองตาม term: " + filtered.size());
    }

    // กรองตามเดือน
    if (month != null) {
        filtered = filtered.stream()
            .filter(fav -> fav.getCreatedAt().getMonthValue() == month)
            .toList();

        System.out.println("📅 หลังกรองตามเดือน: " + filtered.size());
    }

    // กรองตาม status ของหนังสือ
    if (status != null && !status.isBlank()) {
    String normalizedInputStatus = normalizeStatus(status);

    filtered = filtered.stream()
        .filter(fav -> {
            Book b = fav.getBook();
            String bookStatus = Optional.ofNullable(b.getStatus()).orElse("").trim().toLowerCase();
            return bookStatus.equals(normalizedInputStatus);
        })
        .toList();

    System.out.println("🔖 หลังกรองตาม status (" + normalizedInputStatus + "): " + filtered.size());
}


    // รวมรายการหนังสือที่ผ่านเงื่อนไข
    List<Book> books = filtered.stream()
        .map(BookFavorite::getBook)
        .distinct()
        .toList();

    int totalBooks = books.size();
    double totalBudget = books.stream()
        .mapToDouble(b -> Optional.ofNullable(b.getPrice()).orElse(0.0))
        .sum();

    // สร้าง BookItem สำหรับ response
    List<BudgetReportDTO.BookItem> items = books.stream().map(book -> {
        BudgetReportDTO.BookItem item = new BudgetReportDTO.BookItem();
        item.setTitle(book.getBookTitle());
        item.setIsbn(book.getIsbn());
        item.setPrice(book.getPrice());
        item.setPublisher(book.getPublisher());
        item.setYear(book.getYear());
        item.setEdition(book.getEdition());
        item.setStatus(book.getStatus());
        item.setCreatedAt(book.getCreatedAt()); // จาก Book
        return item;
    }).toList();

    BudgetReportDTO dto = new BudgetReportDTO();
    dto.setTotalBooks(totalBooks);
    dto.setTotalBudget(totalBudget);
    dto.setBooks(items);

    System.out.println("📊 ส่งรายงาน: " + totalBooks + " เล่ม | รวมงบประมาณ " + totalBudget);

    return dto;
}


    private int calculateBudgetYear(LocalDateTime created) {
        // ถ้าสร้างหลังเดือนกันยายน (เดือน 10) ให้เพิ่มปีงบประมาณขึ้น 1
        if (created.getMonthValue() >= 10) {
            return created.getYear() + 1;
        }
        return created.getYear();
    }
}
