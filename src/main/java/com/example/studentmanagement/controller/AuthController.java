package com.example.studentmanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.studentmanagement.dto.LoginRequest;
import com.example.studentmanagement.dto.LoginResponse;
import com.example.studentmanagement.dto.RegisterRequest;
import com.example.studentmanagement.dto.RegisterResponse;
import com.example.studentmanagement.dto.RefreshTokenRequest;
import com.example.studentmanagement.dto.RefreshTokenResponse;
import com.example.studentmanagement.entity.RefreshToken;
import com.example.studentmanagement.security.JwtService;
import com.example.studentmanagement.service.RefreshTokenService;
import com.example.studentmanagement.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

        private final UserService userService;
        private final RefreshTokenService refreshTokenService;
        private final JwtService jwtService;

        public AuthController(UserService userService,
                             RefreshTokenService refreshTokenService,
                             JwtService jwtService) {
                this.userService = userService;
                this.refreshTokenService = refreshTokenService;
                this.jwtService = jwtService;
        }

        @PostMapping("/register")
        public ResponseEntity<RegisterResponse> register(
                        @Valid @RequestBody RegisterRequest request) {

                return new ResponseEntity<>(
                                userService.register(request),
                                HttpStatus.CREATED);

        }

        @PostMapping("/login")
        public ResponseEntity<LoginResponse> login(
                        @Valid @RequestBody LoginRequest request,
                        jakarta.servlet.http.HttpServletResponse response) {

                LoginResponse loginResponse = userService.login(request);

                // Set access token cookie
                jakarta.servlet.http.Cookie tokenCookie = new jakarta.servlet.http.Cookie("token", loginResponse.getToken());
                tokenCookie.setHttpOnly(true);
                tokenCookie.setPath("/");
                tokenCookie.setMaxAge(900); // 15 mins (same as jwt expiration)
                response.addCookie(tokenCookie);

                // Set refresh token cookie
                jakarta.servlet.http.Cookie refreshCookie = new jakarta.servlet.http.Cookie("refreshToken", loginResponse.getRefreshToken());
                refreshCookie.setHttpOnly(true);
                refreshCookie.setPath("/");
                refreshCookie.setMaxAge(86400); // 24 hours
                response.addCookie(refreshCookie);

                return ResponseEntity.ok(loginResponse);
        }

        @PostMapping("/refresh")
        public ResponseEntity<RefreshTokenResponse> refresh(
                        @RequestBody(required = false) RefreshTokenRequest request,
                        jakarta.servlet.http.HttpServletRequest httpRequest,
                        jakarta.servlet.http.HttpServletResponse httpResponse) {

                String refreshTokenString = null;
                if (request != null && request.getRefreshToken() != null) {
                        refreshTokenString = request.getRefreshToken();
                } else {
                        // Try to get from cookie
                        if (httpRequest.getCookies() != null) {
                                for (var cookie : httpRequest.getCookies()) {
                                        if ("refreshToken".equals(cookie.getName())) {
                                                refreshTokenString = cookie.getValue();
                                                break;
                                        }
                                }
                        }
                }

                if (refreshTokenString == null || refreshTokenString.isBlank()) {
                        throw new RuntimeException("Refresh token is missing");
                }

                String finalToken = refreshTokenString;
                RefreshToken refreshToken = refreshTokenService.findByToken(finalToken)
                                .map(refreshTokenService::verifyExpiration)
                                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

                String email = refreshToken.getUser().getEmail();
                String newAccessToken = jwtService.generateToken(email);

                // Rotate refresh token
                RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(email);

                // Update cookies
                jakarta.servlet.http.Cookie tokenCookie = new jakarta.servlet.http.Cookie("token", newAccessToken);
                tokenCookie.setHttpOnly(true);
                tokenCookie.setPath("/");
                tokenCookie.setMaxAge(900); // 15 mins
                httpResponse.addCookie(tokenCookie);

                jakarta.servlet.http.Cookie refreshCookie = new jakarta.servlet.http.Cookie("refreshToken", newRefreshToken.getToken());
                refreshCookie.setHttpOnly(true);
                refreshCookie.setPath("/");
                refreshCookie.setMaxAge(86400); // 24 hours
                httpResponse.addCookie(refreshCookie);

                return ResponseEntity.ok(new RefreshTokenResponse(newAccessToken, newRefreshToken.getToken()));
        }

        @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
        public void logout(
                        jakarta.servlet.http.HttpServletRequest httpRequest,
                        jakarta.servlet.http.HttpServletResponse httpResponse) throws java.io.IOException {

                // Clear cookies
                jakarta.servlet.http.Cookie tokenCookie = new jakarta.servlet.http.Cookie("token", "");
                tokenCookie.setHttpOnly(true);
                tokenCookie.setPath("/");
                tokenCookie.setMaxAge(0);
                httpResponse.addCookie(tokenCookie);

                jakarta.servlet.http.Cookie refreshCookie = new jakarta.servlet.http.Cookie("refreshToken", "");
                refreshCookie.setHttpOnly(true);
                refreshCookie.setPath("/");
                refreshCookie.setMaxAge(0);
                httpResponse.addCookie(refreshCookie);

                // Invalidate refresh token in DB if authenticated
                org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
                        try {
                                String email = auth.getName();
                                com.example.studentmanagement.entity.User user = userService.getUserByEmail(email);
                                refreshTokenService.deleteByUser(user);
                        } catch (Exception e) {
                                // ignore
                        }
                }

                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?logout=true");
        }
}