package com.example.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.project.model.AdditionalRequest;
import com.example.project.model.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendNotificationToAdmin(String bookTitle, User user) {
    try {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        // ✅ ใช้ชื่อจริง (first name + last name)
        String fullName = user.getFirstName() + " " + user.getLastName();

        String subject = "แจ้งเตือน: สมาชิกเพิ่มหนังสือในรายการโปรด";
        String text = String.format(
            "เรียน ผู้ดูแลระบบ<br><br>ผู้ใช้ชื่อ <b>%s</b> ได้เพิ่มหนังสือ <b>\"%s\"</b> ลงในรายการโปรด<br><br>ขอบคุณค่ะ,<br>ระบบจัดการหนังสือ ห้องสมุดสมนึก พูนทรัพย์<br>คณะทันตแพทย์ มหาวิทยาลัยขอนแก่น",
            fullName, bookTitle
        );

        helper.setTo("kunyakon.p@kkumail.com");
        helper.setSubject(subject);
        helper.setText(text, true);
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

public void sendAdditionalRequestNotification(AdditionalRequest request, String fullName) {
    try {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        String subject = "แจ้งเตือน: มีคำขอหนังสือเพิ่มเติมจากผู้ใช้";
        String text = String.format(
            "เรียน ผู้ดูแลระบบ<br><br>ผู้ใช้ชื่อ <b>%s</b> ได้ส่งคำขอหนังสือเพิ่มเติม:<br><br>" +
            "<b>ชื่อหนังสือ:</b> %s<br>" +
            "<b>ผู้แต่ง:</b> %s<br>" +
            "<b>สำนักพิมพ์:</b> %s<br>" +
            "<b>ปี:</b> %s<br>" +
            "<b>คำอธิบาย:</b> %s<br><br>" +
            "กรุณาเข้าสู่ระบบเพื่อตรวจสอบรายละเอียดเพิ่มเติม<br><br>ขอบคุณค่ะ,<br>ระบบจัดการหนังสือ ห้องสมุดสมนึก พูนทรัพย์<br>คณะทันตแพทย์ มหาวิทยาลัยขอนแก่น",
            fullName,
            request.getBookTitle(),
            request.getAuthor(),
            request.getPublisher(),
            request.getYear(),
            request.getDescription()
        );

        helper.setTo("kunyakon.p@kkumail.com");
        helper.setSubject(subject);
        helper.setText(text, true);
        helper.setFrom("libdent.bookrequest@gmail.com", "LibDent Book Request");

        mailSender.send(message);
        System.out.println("✅ Email sent successfully");
    } catch (Exception e) {
        System.err.println("❌ Failed to send email: " + e.getMessage());
        e.printStackTrace();
    }
}


}
