package com.example.studentmanagement.dto;

public class LoginResponse {

    private String token;
    private String refreshToken;
    private String role;
    private String name;
    private String email;
    private String message;

    public LoginResponse() {
    }

    public LoginResponse(String token, String refreshToken, String role, String name, String email, String message) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.role = role;
        this.name = name;
        this.email = email;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}