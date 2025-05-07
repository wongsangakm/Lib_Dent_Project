package com.example.project.repository;

import com.example.project.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // ฟังก์ชันสำหรับค้นหาหนังสือโดยชื่อเรื่อง
    List<Book> findByTitleContainingIgnoreCase(String title);

    // ฟังก์ชันสำหรับค้นหาหนังสือโดยผู้เขียน
    List<Book> findByAuthorContainingIgnoreCase(String author);

    // ฟังก์ชันสำหรับค้นหาหนังสือทั้งหมด
    @SuppressWarnings("null")
    List<Book> findAll();

    // ฟังก์ชันสำหรับค้นหาหนังสือที่มี ID ตามที่ระบุ
    Book findById(long id);

    // ฟังก์ชันเพิ่มเติมที่สามารถเขียนเองได้
    // ตัวอย่าง: ค้นหาหนังสือที่มีผู้เขียนตรงกับคำค้นหา
    List<Book> findByAuthor(String author);
}
