package com.github.orisjineman.hexagonalpractice.adapter.out.security;

import com.github.orisjineman.hexagonalpractice.application.port.out.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

@Component
public class JwtTokenProvider implements TokenProvider {

    private final SecretKey secretKey;
    private final long validityInMs;

    public  JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") Long validityInMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.validityInMs = validityInMs;
    }

    public String createToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() +  validityInMs);

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    // 토큰 검증용 메서드는 Port Out 인터페이스엔 없지만,
    // Security Filter에서 직접 이 클래스를 써야 하므로 남겨둠

    // createToken()만 TokenProvider Port Out으로 감쌌고,
    // getEmail()/isValid()(토큰 검증)는 인터페이스에 없이 그대로 둠.
    // 왜냐하면 토큰 발급은 Service(도메인 로직)가 필요로 하는 것이라 Port Out으로 감쌀 가치가 있는데,
    // 토큰 검증은 Security Filter(순수 인프라 계층)에서만 쓰이는 기술적 관심사라서 도메인이 알 필요조차 없는 영역이라서.
    // 이것도 "어디까지 추상화할지"를 판단하는 감각임: 모든 메서드를 무조건 다 Port로 감싸는 게 아니라, "이걸 도메인/애플리케이션이 알아야 하는가?"로 판단.
    public String getEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
