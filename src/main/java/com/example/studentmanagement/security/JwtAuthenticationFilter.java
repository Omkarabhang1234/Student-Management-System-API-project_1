package com.example.studentmanagement.security;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.studentmanagement.entity.RefreshToken;
import com.example.studentmanagement.service.RefreshTokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println(">>> Request URI = " + request.getRequestURI());

        String token = null;
        String refreshTokenString = null;

        // Try extracting access token from Authorization header
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
        }

        // If not in header, try extracting from cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                } else if ("refreshToken".equals(cookie.getName())) {
                    refreshTokenString = cookie.getValue();
                }
            }
        }

        boolean authenticated = false;

        // Try to authenticate using Access Token
        if (token != null && !token.isBlank()) {
            try {
                String email = jwtService.extractUsername(token);
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    if (jwtService.isTokenValid(token, userDetails.getUsername())) {
                        setSecurityContext(userDetails, request);
                        authenticated = true;
                    }
                }
            } catch (Exception e) {
                // Access token expired or invalid, we will fall back to refresh token below
                System.out.println("Access token is invalid or expired: " + e.getMessage());
            }
        }

        // If not authenticated, try using Refresh Token
        if (!authenticated && refreshTokenString != null && !refreshTokenString.isBlank()) {
            try {
                final String finalRefreshTokenString = refreshTokenString;
                Optional<RefreshToken> optRefreshToken = refreshTokenService.findByToken(finalRefreshTokenString);
                if (optRefreshToken.isPresent()) {
                    RefreshToken refreshToken = optRefreshToken.get();
                    // Verify if refresh token is expired
                    refreshTokenService.verifyExpiration(refreshToken);

                    // If valid, auto-generate new Access Token and new rotated Refresh Token
                    String email = refreshToken.getUser().getEmail();
                    String newAccessToken = jwtService.generateToken(email);
                    RefreshToken rotatedRefreshToken = refreshTokenService.createRefreshToken(email);

                    // Add new cookies to the response
                    Cookie tokenCookie = new Cookie("token", newAccessToken);
                    tokenCookie.setHttpOnly(true);
                    tokenCookie.setPath("/");
                    tokenCookie.setMaxAge(900); // 15 mins
                    response.addCookie(tokenCookie);

                    Cookie refreshCookie = new Cookie("refreshToken", rotatedRefreshToken.getToken());
                    refreshCookie.setHttpOnly(true);
                    refreshCookie.setPath("/");
                    refreshCookie.setMaxAge(86400); // 24 hours
                    response.addCookie(refreshCookie);

                    // Set authentication in security context
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    setSecurityContext(userDetails, request);
                    System.out.println("Successfully silently auto-refreshed tokens for: " + email);
                }
            } catch (Exception ex) {
                System.out.println("Failed to authenticate using refresh token: " + ex.getMessage());
                // Invalidate invalid cookies to prevent looping
                Cookie tokenCookie = new Cookie("token", "");
                tokenCookie.setHttpOnly(true);
                tokenCookie.setPath("/");
                tokenCookie.setMaxAge(0);
                response.addCookie(tokenCookie);

                Cookie refreshCookie = new Cookie("refreshToken", "");
                refreshCookie.setHttpOnly(true);
                refreshCookie.setPath("/");
                refreshCookie.setMaxAge(0);
                response.addCookie(refreshCookie);
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setSecurityContext(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}