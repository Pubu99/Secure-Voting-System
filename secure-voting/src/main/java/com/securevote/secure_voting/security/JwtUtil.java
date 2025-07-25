package com.securevote.secure_voting.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    private static final String SECRET_KEY = "YourSecretKey"; // Replace with secure key
    private static final long EXPIRATION_TIME = 86400000; // 1 day in ms

    public String generateToken(String username) {
        log.info("Generating token for username: {}", username);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        try {
            Claims claims = extractClaims(token);
            String username = claims.getSubject();
            log.info("Extracted username from token: {}", username);
            return username;
        } catch (Exception e) {
            log.error("Failed to extract username from token", e);
            return null;
        }
    }

    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        boolean valid = extractedUsername != null && extractedUsername.equals(username);
        log.info("Token validation for user '{}': {}", username, valid);
        return valid;
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
