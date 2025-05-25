package com.supermarket.pqrs.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        userDetails = new User("testuser", "password", Collections.emptyList());
    }

    @Test
    void shouldGenerateTokenWithDefaultClaims() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
    }

    @Test
    void shouldGenerateTokenWithExtraClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ADMIN");
        String token = jwtService.generateToken(claims, userDetails);
        assertNotNull(token);
    }

    @Test
    void shouldExtractUsernameFromToken() {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void shouldValidateValidToken() {
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void shouldDetectInvalidTokenUsernameMismatch() {
        String token = jwtService.generateToken(userDetails);
        UserDetails anotherUser = new User("otro", "password", Collections.emptyList());
        assertFalse(jwtService.isTokenValid(token, anotherUser));
    }

    @Test
    void shouldExtractExpirationClaim() {
        String token = jwtService.generateToken(userDetails);
        Date expiration = jwtService.extractClaim(token, claims -> claims.getExpiration());
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void shouldExtractCustomClaim() {
        String token = jwtService.generateToken(userDetails);
        String subject = jwtService.extractClaim(token, claims -> claims.getSubject());
        assertEquals("testuser", subject);
    }
}
