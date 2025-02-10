package com.example.demo.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.JwtException;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    private static final long EXPIRATION_TIME = 3600000; // Token 有效時間，1小時 (單位：毫秒)

    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
            .setHeaderParam("alg", SignatureAlgorithm.HS256.getValue())
            .setSubject(username)
            .setIssuer("self")
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 1小時過期
            .claim("roles", roles) // 添加角色到 Claims
            .signWith(new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256"), SignatureAlgorithm.HS256)
            .compact();
    }

    public Claims parseToken(String token) throws JwtException {
        Claims claims = null;
        try {
            claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes()) // 設置簽名密鑰
                .build()
                .parseClaimsJws(token) // 驗證並解碼 JWT
                .getBody(); // 獲取 Claims
        } catch (JwtException e) {
            System.err.printf("Invalid JWT token: %s%n", e.getMessage());
            return null;
        }
        return claims;
    }

    public String extractUsername(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return null;
        }
        return claims.getSubject();
    }

    public List<SimpleGrantedAuthority> extractAuthorities(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return null;
        }
        List<String> roles = (List<String>) claims.get("roles"); // 讀取 roles
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
