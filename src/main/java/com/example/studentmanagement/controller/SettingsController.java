package com.example.studentmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.studentmanagement.entity.Settings;
import com.example.studentmanagement.service.SettingsService;

@Controller
public class SettingsController {

    @Autowired
    private SettingsService settingsService;

    // Open Settings Page
    @GetMapping("/settings")
    public String settingsPage(Model model) {

        model.addAttribute("settings", settingsService.getSettings());

        return "settings";
    }

    // Save Settings
    @PostMapping("/settings/save")
    public String saveSettings(@ModelAttribute Settings settings,
            Model model) {

        String result = settingsService.saveSettings(settings);

        model.addAttribute("settings", settingsService.getSettings());

        if ("Saved".equals(result)) {
            model.addAttribute("success", "Settings Saved Successfully!");
        } else {
            model.addAttribute("error", "Password and Confirm Password do not match.");
        }

        return "settings";
    }
}