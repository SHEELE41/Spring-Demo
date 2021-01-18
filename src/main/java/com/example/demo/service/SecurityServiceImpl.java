package com.example.demo.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Service
public class SecurityServiceImpl implements SecurityService {
    private static final String SECRET_KEY = "xQV7zIIBUqeaUjm50jsoOUZ5sLp7D3g3SvPuJpVrjbzT5GWkmWlurs2itflayjAeJ8VjFSqFCiVZlN804bh6693bgxFJTM_xyxP0-RdwPhmIIRLfSWZ6d-0AgPj0OUOErrKo-pPrEYTic3156z5RbivWT2d759A6DRpjAmCe308hS-9e5bc8vBSVu2cEhq5qpCBRVvXAPC9H_vo3fQVkhLTamtVOAXU5vydaz3USDw7q2ibGd7ddsJId6t6f9uIEXpmajteJREjAqjQ5AJCEX3kWn1IxmwQNVbdELPC3qCCmGmRXZ4Vf65J5cHSSEqWKWNVgbz-Qlt0hYLi7qvEqW8oGsOzM0yITo0fJetd4DZLBoOYMrALTm7UPX1sI8WlpyloSMAqgfTIe7QuwmMScb1I0b4UJzKknYU29Sc5hmbWD-lFhdzvCPNfAycnSuCen-M_M1pOiD4P2Y80P6e6y3dg1DnLqibhyCTZHyUhM641m9JiMKEH1NI3Cd9f6QH8mQ";

    @Override
    public String createToken(String subject, long ttlMillis) {
        if (ttlMillis <= 0) {
            throw new RuntimeException("Expire time must be greater than zero : [" + ttlMillis + "]");
        }
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        byte[] secretKeyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName());
        return Jwts.builder()
                .setSubject(subject)
                .signWith(signingKey, signatureAlgorithm)
                .setExpiration(new Date(System.currentTimeMillis() + ttlMillis))
                .compact();
    }

    @Override
    public String getSubject(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
