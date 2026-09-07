package com.example.studentmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.studentmanagement.service.StudentService;

@Controller
@RequestMapping("/user")
public class UserController {

    private final StudentService studentService;

    public UserController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalStudents", studentService.getTotalStudents());
        return "user/dashboard";
    }

    @GetMapping("/profile")
    public String profile() {
        return "user-profile";
    }
}