package com.example.studentmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.studentmanagement.service.StudentService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final StudentService studentService;

    public AdminController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("totalStudents", studentService.getTotalStudents());
        return "admin/dashboard";
    }

    @GetMapping("/profile")
    public String profile() {
        return "admin-profile";
    }
}