package com.example.studentmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.studentmanagement.entity.Settings;

public interface SettingsRepository extends JpaRepository<Settings, Long> {

}