package com.example.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendNotificationToAdmin(String bookTitle, String memberEmail) {
        String to = "kunyakon.p@kkumail.com";
        String subject = "แจ้งเตือน: สมาชิกได้เพิ่มหนังสือลง Favorite";
        String text = String.format("สมาชิกอีเมล %s ได้เพิ่มหนังสือ \"%s\" ลงในรายการโปรดของเขา\n\nขอแสดงความนับถือ\nระบบจัดการหนังสือ ห้องสมุดทันตแพทย์ มหาวิทยาลัยขอนแก่น", 
    memberEmail, bookTitle);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("libdent.bookrequest@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        

        mailSender.send(message);
    }
}
