package com.example.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendNotificationToAdmin(String bookTitle, String memberEmail) {
    try {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        String subject = "แจ้งเตือน: สมาชิกเพิ่มหนังสือในรายการโปรด";
        String text = String.format(
            "เรียน ผู้ดูแลระบบ<br><br>ผู้ใช้ชื่อ <b>%s</b> ได้เพิ่มหนังสือ <b>\"%s\"</b> ลงในรายการโปรด<br><br>ขอบคุณค่ะ,<br>ระบบจัดการหนังสือ ห้องสมุดทันตแพทย์ มหาวิทยาลัยขอนแก่น",
            memberEmail, bookTitle
        );

        helper.setTo("kunyakon.p@kkumail.com");
        helper.setSubject(subject);
        helper.setText(text, true); // true = HTML
        helper.setFrom("libdent.bookrequest@gmail.com", "LibDent Book Request");
        helper.setReplyTo("libdent.bookrequest@gmail.com");

        mailSender.send(message);
        System.out.println("✅ Email sent successfully");
    } catch (MessagingException e) {
        System.err.println("❌ Failed to send email: " + e.getMessage());
        e.printStackTrace();
    } catch (Exception ex) {
        System.err.println("❌ Unexpected error: " + ex.getMessage());
    }
}

}
