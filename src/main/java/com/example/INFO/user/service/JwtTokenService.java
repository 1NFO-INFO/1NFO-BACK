package com.example.INFO.user.service;

import com.example.INFO.user.dto.JwtTokenDto;
import com.example.INFO.user.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final JwtProperties jwtProperties;

    private SecretKey getSecretKey() {
        byte[] keyBytes = jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsername(String token) {
        return extractClaims(token).get("username", String.class);
    }

    public LocalDateTime getIssuedAt(String token) {
        Date expiration = extractClaims(token).getIssuedAt();
        return dateToLocalDateTime(expiration);
    }

    public LocalDateTime getExpiration(String token) {
        Date expiration = extractClaims(token).getExpiration();
        return dateToLocalDateTime(expiration);
    }

    public boolean isExpired(String token) {
        Date expiredDate = extractClaims(token).getExpiration();
        return expiredDate.before(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public JwtTokenDto generateJwtToken(String username) {
        Date now = new Date();

        String accessToken = generateAccessToken(username, now);
        String refreshTokenDto = generateRefreshToken(username, now);

        return JwtTokenDto.of(accessToken, refreshTokenDto);
    }

    private String generateAccessToken(String username, Date now) {
        Claims claims = Jwts.claims()
                .add("username", username)
                .build();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + jwtProperties.getAccessTokenExpiredTimeSec() * 1000))
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    private String generateRefreshToken(String username, Date now) {
        Claims claims = Jwts.claims()
                .add("username", username)
                .build();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + jwtProperties.getRefreshTokenExpiredTimeSec() * 1000))
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    private LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
