package com.example.project.service;

import com.example.project.payload.request.BudgetReportDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ReportExportService {

    public byte[] exportToExcel(List<BudgetReportDTO> books) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Report");

            Row header = sheet.createRow(0);
            String[] columns = {"ชื่อหนังสือ", "ISBN", "ราคา", "สำนักพิมพ์", "สถานะ"};
            for (int i = 0; i < columns.length; i++) {
                header.createCell(i).setCellValue(columns[i]);
            }

            int rowIdx = 1;
            for (BudgetReportDTO book : books) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(book.getTitle());
                row.createCell(1).setCellValue(book.getIsbn());
                row.createCell(2).setCellValue(book.getPrice());
                row.createCell(3).setCellValue(book.getPublisher());
                row.createCell(4).setCellValue(book.getStatus());
            }

            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error exporting Excel", e);
        }
    }

    public byte[] exportToPdf(List<BudgetReportDTO> books) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph title = new Paragraph("รายงานหนังสือ", font);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            Stream.of("ชื่อหนังสือ", "ISBN", "ราคา", "สำนักพิมพ์", "สถานะ").forEach(col -> {
                PdfPCell cell = new PdfPCell(new Phrase(col));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                table.addCell(cell);
            });

            for (BudgetReportDTO book : books) {
                table.addCell(book.getTitle());
                table.addCell(book.getIsbn());
                table.addCell(String.valueOf(book.getPrice()));
                table.addCell(book.getPublisher());
                table.addCell(book.getStatus());
            }

            document.add(table);
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error exporting PDF", e);
        }
    }
}
