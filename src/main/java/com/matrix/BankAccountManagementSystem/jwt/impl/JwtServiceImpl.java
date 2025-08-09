package com.matrix.BankAccountManagementSystem.jwt.impl;

import com.matrix.BankAccountManagementSystem.jwt.JwtService;
import com.matrix.BankAccountManagementSystem.model.entity.security.User;
import com.matrix.BankAccountManagementSystem.model.entity.security.Authority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    @Value("${issue.key}")
    private String issueKey;

    @PostConstruct
    public Key secretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(issueKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String issueToken(User user) {
        List<String> authorities = user
                .getAuthorities()
                .stream()
                .map(Authority::getAuthority)
                .toList();
        final JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(Duration.ofSeconds(6000))))
                .addClaims(Map.of("roles", authorities))
                .setHeader(Map.of("type", "JWT"))
                .signWith(secretKey(), SignatureAlgorithm.HS256);
        return jwtBuilder.compact();
    }
    @Override
    public Claims verifyToken(String token){
        return Jwts.parser()
                .setSigningKey(secretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
