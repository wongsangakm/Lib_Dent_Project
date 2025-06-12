package com.example.project.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.model.AdditionalRequest;
import com.example.project.model.Book;
import com.example.project.model.BookFavorite;
import com.example.project.payload.request.BudgetReportDTO;
import com.example.project.repository.AdditionalRequestRepository;
import com.example.project.repository.FavoriteRepository;

@Service
public class BudgetService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private AdditionalRequestRepository additionalRequestRepository;

    @Autowired
    private ReportExportService reportExportService;

    private String normalizeStatus(String status) {
        if (status == null) return null;
        return switch (status.trim()) {
            case "กำลังสั่งซื้อ" -> "pending";
            case "สั่งซื้อแล้ว" -> "ordered";
            case "pending", "ordered" -> status;
            default -> status.toLowerCase();
        };
    }

    // เมธอดหลักสำหรับสร้างรายงานงบประมาณ
    public BudgetReportDTO generateReport(Integer year, Integer term, Integer month, String status) {
        List<BookFavorite> favorites = favoriteRepository.findAll();
        List<AdditionalRequest> additionalRequests = additionalRequestRepository.findAll();

        // กรอง BookFavorite ตามปีงบประมาณ
        List<BookFavorite> filtered = favorites.stream()
            .filter(fav -> {
                LocalDateTime created = fav.getCreatedAt();
                if (created == null) return false;
                int budgetYear = calculateBudgetYear(created);
                return budgetYear == year;
            })
            .toList();

        // กรอง BookFavorite ตามเทอม
        if (term != null) {
            Set<Integer> months = switch (term) {
                case 1 -> Set.of(5, 6, 7, 8, 9);
                case 2 -> Set.of(11, 12, 1, 2, 3);
                case 3 -> Set.of(5, 6, 7, 8, 9, 11, 12, 1, 2, 3);
                default -> throw new IllegalArgumentException("Invalid term: " + term);
            };

            filtered = filtered.stream()
                .filter(fav -> months.contains(fav.getCreatedAt().getMonthValue()))
                .toList();
        }

        // กรอง BookFavorite ตามเดือน
        if (month != null) {
            filtered = filtered.stream()
                .filter(fav -> fav.getCreatedAt().getMonthValue() == month)
                .toList();
        }

        // กรอง BookFavorite ตามสถานะ
        if (status != null && !status.isBlank()) {
            String normalizedInputStatus = normalizeStatus(status);
            filtered = filtered.stream()
                .filter(fav -> {
                    Book b = fav.getBook();
                    String bookStatus = Optional.ofNullable(b.getStatus()).orElse("").trim().toLowerCase();
                    return bookStatus.equals(normalizedInputStatus);
                })
                .toList();
        }

        // กรอง AdditionalRequest ตามปีงบประมาณ
        List<AdditionalRequest> filteredAdditionalRequests = additionalRequests.stream()
            .filter(req -> {
                Date date = req.getRequestDate();
                if (date == null) return false;
                LocalDateTime created = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                int budgetYear = calculateBudgetYear(created);
                return budgetYear == year;
            })
            .toList();

        // กรอง AdditionalRequest ตามเทอม
        if (term != null) {
            Set<Integer> months = switch (term) {
                case 1 -> Set.of(5, 6, 7, 8, 9);
                case 2 -> Set.of(11, 12, 1, 2, 3);
                case 3 -> Set.of(5, 6, 7, 8, 9, 11, 12, 1, 2, 3);
                default -> throw new IllegalArgumentException("Invalid term: " + term);
            };

            filteredAdditionalRequests = filteredAdditionalRequests.stream()
                .filter(req -> {
                    Date date = req.getRequestDate();
                    if (date == null) return false;
                    LocalDateTime created = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    return months.contains(created.getMonthValue());
                })
                .toList();
        }

        // กรอง AdditionalRequest ตามเดือน
        if (month != null) {
            filteredAdditionalRequests = filteredAdditionalRequests.stream()
                .filter(req -> {
                    Date date = req.getRequestDate();
                    if (date == null) return false;
                    LocalDateTime created = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    return created.getMonthValue() == month;
                })
                .toList();
        }

        // กรอง AdditionalRequest ตามสถานะ (และไม่เอา rejected)
        if (status != null && !status.isBlank()) {
            String normalizedStatus = normalizeStatus(status);
            filteredAdditionalRequests = filteredAdditionalRequests.stream()
                .filter(req -> {
                    String reqStatus = Optional.ofNullable(req.getStatus()).orElse("").trim().toLowerCase();
                    return reqStatus.equals(normalizedStatus);
                })
                .toList();
        }

        // ตัด rejected ออก
        filteredAdditionalRequests = filteredAdditionalRequests.stream()
            .filter(req -> {
                String statusVal = Optional.ofNullable(req.getStatus()).orElse("").trim().toLowerCase();
                return !statusVal.equals("rejected");
            })
            .toList();

        // สร้าง BookItem จาก BookFavorite
        List<Book> books = filtered.stream()
            .map(BookFavorite::getBook)
            .distinct()
            .toList();

        List<BudgetReportDTO.BookItem> items = books.stream()
            .map(book -> {
                BudgetReportDTO.BookItem item = new BudgetReportDTO.BookItem();
                item.setTitle(book.getBookTitle());
                item.setIsbn(book.getIsbn() != null ? String.valueOf(book.getIsbn()) : null);
                item.setPrice(book.getPrice());
                item.setPublisher(book.getPublisher());
                item.setYear(book.getYear() != null ? String.valueOf(book.getYear()) : null);
                item.setEdition(book.getEdition());
                item.setStatus(book.getStatus());
                item.setCreatedAt(book.getCreatedAt());
                return item;
            })
            .collect(Collectors.toList());

        // สร้าง BookItem จาก AdditionalRequest
        List<BudgetReportDTO.BookItem> requestItems = filteredAdditionalRequests.stream()
            .map(req -> {
                BudgetReportDTO.BookItem item = new BudgetReportDTO.BookItem();
                item.setTitle(req.getBookTitle());
                item.setIsbn(req.getIsbn() != null ? String.valueOf(req.getIsbn()) : null);
                item.setPrice(req.getPrice());
                item.setPublisher(req.getPublisher());
                item.setYear(req.getYear() != null ? String.valueOf(req.getYear()) : null);
                item.setStatus(req.getStatus());
                item.setCreatedAt(
                    req.getRequestDate() == null ? null
                    : req.getRequestDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                );
                return item;
            })
            .collect(Collectors.toList());

        // รวมยอดรวม
        int totalBooks = items.size() + requestItems.size();
        double totalBudget = Stream.concat(items.stream(), requestItems.stream())
            .mapToDouble(b -> Optional.ofNullable(b.getPrice()).orElse(0.0))
            .sum();

        // ส่งกลับ
        BudgetReportDTO dto = new BudgetReportDTO();
        dto.setTotalBooks(totalBooks);
        dto.setTotalBudget(totalBudget);
        dto.setBooks(items);
        dto.setAdditionalRequests(requestItems); // 🟢 ใส่แยก

        return dto;
    }

    private int calculateBudgetYear(LocalDateTime created) {
        return (created.getMonthValue() >= 10) ? created.getYear() + 1 : created.getYear();
    }

    // แก้ไขลำดับพารามิเตอร์ให้ตรงกัน (year, term, month, status)
    public byte[] generateExcel(Integer year, Integer term, Integer month, String status) {
        List<BudgetReportDTO> data = getBudgetReportData(year, term, month, status);
        return reportExportService.exportToExcel(data);
    }

    public byte[] generatePdf(Integer year, Integer term, Integer month, String status) {
        List<BudgetReportDTO> data = getBudgetReportData(year, term, month, status);
        return reportExportService.exportToPdf(data);
    }

    // Implement getBudgetReportData โดยเรียก generateReport แล้ว return List
    private List<BudgetReportDTO> getBudgetReportData(Integer year, Integer term, Integer month, String status) {
        BudgetReportDTO dto = generateReport(year, term, month, status);
        return List.of(dto);
    }

}
