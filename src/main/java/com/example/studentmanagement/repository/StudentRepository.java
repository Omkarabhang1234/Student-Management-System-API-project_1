package com.example.studentmanagement.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.studentmanagement.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Search By Name
    List<Student> findByName(String name);

    // Search By Email
    boolean existsByEmail(String email);

    List<Student> findByEmail(String email);

    // Search By Department
    List<Student> findByDepartment(String department);

    // Search By City
    List<Student> findByCity(String city);

    // Pagination
    Page<Student> findAll(Pageable pageable);

}