package com.example.studentmanagement.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "settings")
public class Settings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private String confirmPassword;

    private String adminName;

    private String email;

    private String mobile;

    private String appName;

    private String collegeName;

    private String defaultDepartment;

    private String defaultCity;

    private Boolean registrationEnabled;

    private String theme;

    private String password;

    public Settings() {
    }

    public Settings(Long id, String adminName, String email,
            String mobile, String appName,
            String collegeName,
            String defaultDepartment,
            String defaultCity,
            Boolean registrationEnabled,
            String theme,
            String password) {

        this.id = id;
        this.adminName = adminName;
        this.email = email;
        this.mobile = mobile;
        this.appName = appName;
        this.collegeName = collegeName;
        this.defaultDepartment = defaultDepartment;
        this.defaultCity = defaultCity;
        this.registrationEnabled = registrationEnabled;
        this.theme = theme;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getDefaultDepartment() {
        return defaultDepartment;
    }

    public void setDefaultDepartment(String defaultDepartment) {
        this.defaultDepartment = defaultDepartment;
    }

    public String getDefaultCity() {
        return defaultCity;
    }

    public void setDefaultCity(String defaultCity) {
        this.defaultCity = defaultCity;
    }

    public Boolean getRegistrationEnabled() {
        return registrationEnabled;
    }

    public void setRegistrationEnabled(Boolean registrationEnabled) {
        this.registrationEnabled = registrationEnabled;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}