package com.example.advance.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
@Service
public class JwtUtil {

    // 기본 셋팅
    public static String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 60 * 60 * 1000L;// 발급한 토큰의 시간을 60분 설정했음.

    private SecretKey key;
    private JwtParser parser;

    @Value("${jwt.secret.key}")
    private String secretKeyString;

    /**
     * 빈 초기화 메서드
     * @PostConstruct 어플리케이션 실행 될 때 가장 먼저 실행 되게 하는 어노테이션 */

    @PostConstruct
    public void init() {
        byte[] bytes = Decoders.BASE64.decode(secretKeyString);
        this.key = Keys.hmacShaKeyFor(bytes);
        this.parser = Jwts.parser()
                .verifyWith(this.key)
                .build();
    }

    // 토큰 생성
    // username이란 값을 넣어 준다. 다른 값 추가도 가능 예시로 추가
    // '발행 시간' issuedAt(now)
    // '만료 시간' .expiration(new Date(now.getTime() + TOKEN_TIME))
    public String generateToken(String username) {
        Date now = new Date();
        return BEARER_PREFIX + Jwts.builder()
                .claim("username", username)
                .issuedAt(now) // 발행 시간
                .expiration(new Date(now.getTime() + TOKEN_TIME)) // 만료 시간
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        if (token == null || token.isBlank()) return false;
        try {
            parser.parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 개별 예외 분리 없음: 서명/형식/만료 등 모든 실패를 한 번에 처리
            log.info("Invalid JWT: {}", e.toString());
            return false;
        }
    }

    // 토큰 복호화
    private Claims extractAllClaims(String token) {
        return parser.parseSignedClaims(token).getPayload();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).get("username", String.class);
    }


}
