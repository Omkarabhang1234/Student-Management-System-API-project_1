package com.example.studentmanagement.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key getKey() {
        return new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(String email) {

        return Jwts.builder()

                .subject(email)

                .issuedAt(new Date())

                .expiration(new Date(System.currentTimeMillis() + expiration))

                .signWith(getKey())

                .compact();
    }

    public String extractUsername(String token) {

        return getClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String email) {

        return extractUsername(token).equals(email)
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {

        return getClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private Claims getClaims(String token) {

        return Jwts.parser()

                .verifyWith((javax.crypto.SecretKey) getKey())

                .build()

                .parseSignedClaims(token)

                .getPayload();
    }
}