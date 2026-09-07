package com.example.studentmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String toEmail, String studentName) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Welcome to Student Management System");

        message.setText(
                "Dear " + studentName + ",\n\n"
                        + "Congratulations! Your registration has been completed successfully.\n\n"
                        + "Welcome to Student Management System.\n\n"
                        + "Regards,\n"
                        + "Omkar Abhang\n"
                        + "System Administrator");

        mailSender.send(message);
    }
}