package com.example.project.service;

import com.example.project.model.Book;
import com.example.project.repository.BookRepository;
import java.util.concurrent.CompletableFuture;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class BookMetadataScraper {

    @Autowired
    private BookRepository bookRepository;

    @Async
    public CompletableFuture<Void> fetchAndUpdateBookMetadata(Book book) {
        try {
            Document doc = Jsoup.connect(book.getUrl()).userAgent("Mozilla").get();

            Element img = doc.selectFirst("meta[property=og:image]");
            if (img != null) book.setCoverImage(img.attr("content"));

            String desc = fetchDescription(book.getUrl());
            if (desc != null && desc.length() > 2000) {
                desc = desc.substring(0, 2000); // ป้องกันยาวเกิน column
            }
            book.setDescription(desc);

            bookRepository.save(book);
        } catch (Exception e) {
            System.err.println("Error fetching metadata: " + e.getMessage());
        }

        return CompletableFuture.completedFuture(null);
    }

    public String fetchDescription(String url) {
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
    
            // ✅ เน้นเอาจาก <section id="about-this-book"> เท่านั้น
            Element aboutSection = doc.selectFirst("section#about-this-book");
            if (aboutSection != null) {
                // รวมข้อความทั้งหมดจาก <p> ด้านใน
                StringBuilder description = new StringBuilder();
                for (Element p : aboutSection.select("p")) {
                    description.append(p.text()).append("\n");
                }
                String result = description.toString().trim();
                return result.isBlank() ? null : result;
            }
    
            return null;
        } catch (Exception e) {
            System.err.println("⚠️ Error fetching description: " + e.getMessage());
            return null;
        }
    }
    
    
    
}
