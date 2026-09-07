package com.example.studentmanagement.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.studentmanagement.entity.Role;
import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Seed default Admin if not exists
        if (!userRepository.existsByEmail("admin@sms.com")) {
            User admin = new User();
            admin.setName("Admin User");
            admin.setEmail("admin@sms.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);
            userRepository.save(admin);
            System.out.println("========== Seeded Default Admin (admin@sms.com / admin123) ==========");
        }

        // Seed default User if not exists
        if (!userRepository.existsByEmail("user@sms.com")) {
            User user = new User();
            user.setName("Regular User");
            user.setEmail("user@sms.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole(Role.USER);
            user.setEnabled(true);
            userRepository.save(user);
            System.out.println("========== Seeded Default User (user@sms.com / user123) ==========");
        }
    }
}
