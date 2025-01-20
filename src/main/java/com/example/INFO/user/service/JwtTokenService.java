package com.example.INFO.user.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final SecretKey key;

    @Value("${jwt.token.expired-time-ms}")
    private long expiredTimeMs;

    public String getUsername(String token) {
        return extractClaims(token).get("username", String.class);
    }

    public boolean isExpired(String token) {
        Date expiredDate = extractClaims(token).getExpiration();
        return expiredDate.before(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(String username) {
        Claims claims = Jwts.claims()
                .add("username", username)
                .build();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }
}
