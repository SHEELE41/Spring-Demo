package com.example.demo.util;

import com.example.demo.model.UserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private final String SECRET_KEY = "xQV7zIIBUqeaUjm50jsoOUZ5sLp7D3g3SvPuJpVrjbzT5GWkmWlurs2itflayjAeJ8VjFSqFCiVZlN804bh6693bgxFJTM_xyxP0-RdwPhmIIRL";

    public int extractUserId(String token) {
        final Claims claims = extractAllClaims(token);
        return (int) claims.get("userId");
    }

    public String extractUsername(String token) {
        final Claims claims = extractAllClaims(token);
        return (String) claims.get("username");
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 생성
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        double dValue = Math.random();
        int iValue = (int)(dValue * 10);
        claims.put("userId", 4);    // iValue
        claims.put("username", userDetails.getUsername());
        return createToken(claims);
    }

    private String createToken(Map<String, Object> claims) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] secretKeyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject("User Login Token")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(signingKey, signatureAlgorithm)
                .compact();
    }

    // 토큰이 유효한지 검증
    public Boolean validateToken(String token) {
        if (!token.isEmpty()) {
            try {
                Jwts.parserBuilder()
                        .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                        .build()
                        .parseClaimsJws(token);
                return true;
            } catch (SignatureException e) {
                log.error("Invalid JWT signature", e);
            } catch (MalformedJwtException e) {
                log.error("Invalid JWT token", e);
            } catch (ExpiredJwtException e) {
                log.error("Expired JWT token", e);
            } catch (UnsupportedJwtException e) {
                log.error("Unsupported JWT token", e);
            } catch (IllegalArgumentException e) {
                log.error("JWT claims string is empty.", e);
            }
        }
        return false;
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
