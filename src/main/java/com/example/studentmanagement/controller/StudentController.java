package com.example.studentmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.service.StudentService;

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    // =========================
    // Dashboard
    // =========================

    private boolean checkIsAdmin() {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        }
        return false;
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        if (checkIsAdmin()) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/user/dashboard";
        }
    }

    // =========================
    // Add Student Page
    // =========================

    @GetMapping("/add-student")
    public String addStudentPage(Model model) {

        model.addAttribute("student", new Student());

        return "add-student";
    }

    // =========================
    // Save Student
    // =========================

    @PostMapping("/students")
    public String saveStudent(@ModelAttribute Student student) {

        studentService.addStudent(student);

        return "redirect:/dashboard";
    }

    // =========================
    // Student List
    // =========================

    @GetMapping("/api/students")
    public String studentList(Model model) {

        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("isAdmin", checkIsAdmin());

        return "student-list";
    }

    // =========================
    // Delete Student
    // =========================

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {

        studentService.deleteStudent(id);

        return "redirect:/students";
    }

    // =========================
    // Edit Student Page
    // =========================

    @GetMapping("/edit/{id}")
    public String editStudent(@PathVariable Long id,
            Model model) {

        Student student = studentService.getStudentById(id);

        model.addAttribute("student", student);

        return "edit-student";
    }

    // =========================
    // Update Student
    // =========================

    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable Long id,
            @ModelAttribute Student student) {

        studentService.updateStudent(id, student);

        return "redirect:/students";
    }

    // =========================
    // Search By Name
    // =========================

    @GetMapping("/search/name")
    public String searchByName(@RequestParam String name,
            Model model) {

        model.addAttribute("students",
                studentService.searchByName(name));
        model.addAttribute("isAdmin", checkIsAdmin());

        return "student-list";
    }

    // =========================
    // Search By Email
    // =========================

    @GetMapping("/search/email")
    public String searchByEmail(@RequestParam String email,
            Model model) {

        model.addAttribute("students",
                studentService.searchByEmail(email));
        model.addAttribute("isAdmin", checkIsAdmin());

        return "student-list";
    }

    // =========================
    // Search By Department
    // =========================

    @GetMapping("/search/department")
    public String searchByDepartment(@RequestParam String department,
            Model model) {

        model.addAttribute("students",
                studentService.searchByDepartment(department));
        model.addAttribute("isAdmin", checkIsAdmin());

        return "student-list";
    }

    // =========================
    // Search By City
    // =========================

    @GetMapping("/search/city")
    public String searchByCity(@RequestParam String city,
            Model model) {

        model.addAttribute("students",
                studentService.searchByCity(city));
        model.addAttribute("isAdmin", checkIsAdmin());

        return "student-list";
    }

    // =========================
    // Pagination
    // =========================

    @GetMapping("/students/page")
    public String pagination(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        Page<Student> studentPage = studentService.getStudents(page, size);

        model.addAttribute("students",
                studentPage.getContent());

        model.addAttribute("currentPage", page);

        model.addAttribute("totalPages",
                studentPage.getTotalPages());
        model.addAttribute("isAdmin", checkIsAdmin());

        return "student-list";
    }

    // =========================
    // Reports
    // =========================

    @GetMapping("/reports")
    public String reports(Model model) {

        model.addAttribute("students", studentService.getAllStudents());

        model.addAttribute("totalStudents", studentService.getTotalStudents());

        return "reports";
    }

    // =========================
    // User - View All Students
    // =========================

    @GetMapping("/student/all")
    public String allStudents(Model model) {

        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("isAdmin", checkIsAdmin());

        return "student-list";
    }

    @GetMapping("/admin-profile")
    public String adminProfile() {
        return "admin-profile";
    }

}
