package com.example.studentmanagement.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordController {

    @GetMapping("/generate-password")
    public String generatePassword() {

        System.out.println(">>> PasswordController called");

        return new BCryptPasswordEncoder().encode("admin123");
    }
}