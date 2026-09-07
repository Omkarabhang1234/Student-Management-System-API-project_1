package com.example.studentmanagement.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.example.studentmanagement.entity.User;
import com.example.studentmanagement.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        System.out.println("========== User Loaded ==========");
        System.out.println("Email   : " + user.getEmail());
        System.out.println("Role    : " + user.getRole());
        System.out.println("Enabled : " + user.getEnabled());
        System.out.println("Password: " + user.getPassword());
        System.out.println("=================================");

        return new CustomUserDetails(user);
    }
}