package com.example.demo.util;

import com.example.demo.model.UserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtUtilTest {
    JwtUtil jwtUtil;
    UserDetails userDetails;

    @BeforeEach
    public void beforeEach() {
        jwtUtil = new JwtUtil();
        userDetails = new UserDetails();
    }

    @Test
    public void extractUsername() {
        userDetails.setUsername("SEOK JONG KYU");
        String token = jwtUtil.generateToken(userDetails);
        String extractedUsername = jwtUtil.extractUsername(token);

        assertEquals(extractedUsername, userDetails.getUsername());
    }

    @Test
    public void extractExpiration() {
        userDetails.setUsername("SEOK JONG KYU");
        String token = jwtUtil.generateToken(userDetails);
        assertTrue((new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)).compareTo(jwtUtil.extractExpiration(token)) <= 1);
    }

    @Test
    public void generateToken() {
        userDetails.setUsername("SEOK JONG KYU");
        String token = jwtUtil.generateToken(userDetails);

    }

    @Test
    public void validateToken() {
        userDetails.setUsername("SEOK JONG KYU");
        String token = jwtUtil.generateToken(userDetails);

        assertTrue(jwtUtil.validateToken(token, userDetails));
    }
}
