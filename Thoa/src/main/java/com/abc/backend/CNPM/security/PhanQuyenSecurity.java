package com.abc.backend.CNPM.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class PhanQuyenSecurity {

    @Value("${app.jwt.secret:thuexe-secret-key-at-least-256-bits-long-for-hs256}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms:86400000}")
    private long jwtExpirationMs;

    // ===================== TẠO TOKEN =====================

    public String taoToken(Long nguoiDungId, String email, String vaiTro) {
        return Jwts.builder()
                .setSubject(email)
                .claim("nguoiDungId", nguoiDungId)
                .claim("vaiTro", vaiTro)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(layKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ===================== ĐỌC TOKEN =====================

    public Long layNguoiDungId(String token) {
        Claims claims = layAllClaims(token);
        return claims.get("nguoiDungId", Long.class);
    }

    public String layEmail(String token) {
        return layAllClaims(token).getSubject();
    }

    public String layVaiTro(String token) {
        return layAllClaims(token).get("vaiTro", String.class);
    }

    // ===================== XÁC THỰC TOKEN =====================

    public boolean validateToken(String token) {
        try{Jwts.parser()
                .verifyWith(layKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (SecurityException e) {
            log.error("Chữ ký JWT không hợp lệ: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("JWT không đúng định dạng: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT đã hết hạn: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT không được hỗ trợ: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT rỗng: {}", e.getMessage());
        }
        return false;
    }

    // ===================== PRIVATE =====================

    private Claims layAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(layKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey layKey() {
        return Keys.hmacShaKeyFor(
                jwtSecret.getBytes(StandardCharsets.UTF_8)
        );
    }
}