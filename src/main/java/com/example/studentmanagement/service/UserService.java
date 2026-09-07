package com.example.studentmanagement.service;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.studentmanagement.dto.LoginRequest;
import com.example.studentmanagement.dto.LoginResponse;
import com.example.studentmanagement.dto.RegisterRequest;
import com.example.studentmanagement.dto.RegisterResponse;
import com.example.studentmanagement.entity.Role;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.entity.RefreshToken;
import com.example.studentmanagement.repository.UserRepository;
import com.example.studentmanagement.security.JwtService;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public UserService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            RefreshTokenService refreshTokenService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    // ==========================
    // Register User
    // ==========================
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists.");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setEnabled(true);

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole().name(),
                "Registration Successful");
    }

    // ==========================
    // Login User
    // ==========================
    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        String token = jwtService.generateToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getEmail());

        return new LoginResponse(
                token,
                refreshToken.getToken(),
                user.getRole().name(),
                user.getName(),
                user.getEmail(),
                "Login Successful");
    }

    // ==========================
    // Get User By Email
    // ==========================
    public User getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
    }

    // ==========================
    // Update Profile
    // ==========================
    public User updateProfile(User updatedUser) {

        User user = userRepository.findById(updatedUser.getId())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        user.setName(updatedUser.getName());

        if (updatedUser.getPassword() != null &&
                !updatedUser.getPassword().isBlank()) {

            user.setPassword(
                    passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(user);
    }

    // ==========================
    // Change Role
    // ==========================
    public void changeRole(Long id, Role role) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        user.setRole(role);

        userRepository.save(user);
    }

    // ==========================
    // Enable User
    // ==========================
    public void enableUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        user.setEnabled(true);

        userRepository.save(user);
    }

    // ==========================
    // Disable User
    // ==========================
    public void disableUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        user.setEnabled(false);

        userRepository.save(user);
    }

    // ==========================
    // Find User By ID
    // ==========================
    public Optional<User> getUser(Long id) {

        return userRepository.findById(id);
    }

    // ==========================
    // Delete User
    // ==========================
    public void deleteUser(Long id) {

        userRepository.deleteById(id);
    }
}