package com.example.studentmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.repository.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailService emailService;

    // Add Student
    public Student addStudent(Student student) {

        Student savedStudent = studentRepository.save(student);

        emailService.sendWelcomeEmail(
                savedStudent.getEmail(),
                savedStudent.getName());

        return savedStudent;
    }

    // View All Students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get Student By Id
    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    // Update Student
    public Student updateStudent(Long id, Student studentDetails) {

        Student student = studentRepository.findById(id).orElse(null);

        if (student != null) {

            student.setName(studentDetails.getName());
            student.setEmail(studentDetails.getEmail());
            student.setDepartment(studentDetails.getDepartment());
            student.setCity(studentDetails.getCity());

            return studentRepository.save(student);
        }

        return null;
    }

    // Delete Student
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    // Total Students
    public long getTotalStudents() {
        return studentRepository.count();
    }

    // Search By Name
    public List<Student> searchByName(String name) {
        return studentRepository.findByName(name);
    }

    // Search By Email
    public List<Student> searchByEmail(String email) {
        return studentRepository.findByEmail(email);
    }

    // Search By Department
    public List<Student> searchByDepartment(String department) {
        return studentRepository.findByDepartment(department);
    }

    // Search By City
    public List<Student> searchByCity(String city) {
        return studentRepository.findByCity(city);
    }

    // Pagination
    public Page<Student> getStudents(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return studentRepository.findAll(pageable);

    }

}