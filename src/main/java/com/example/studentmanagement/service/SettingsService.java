package com.example.studentmanagement.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.studentmanagement.entity.Settings;
import com.example.studentmanagement.repository.SettingsRepository;

@Service
public class SettingsService {

    @Autowired
    private SettingsRepository repository;

    public Settings getSettings() {

        Optional<Settings> settings = repository.findById(1L);

        return settings.orElse(new Settings());
    }

    public String saveSettings(Settings settings) {

        if (!settings.getPassword()
                .equals(settings.getConfirmPassword())) {

            return "Password Mismatch";
        }

        settings.setId(1L);

        repository.save(settings);

        return "Saved";
    }

}