package com.github.orisjineman.hexagonalpractice.adapter.in.web;

import com.github.orisjineman.hexagonalpractice.adapter.out.security.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter { // OncePerRequestFilter: Spring Security가 제공하는 베이스 클래스, 요청당 딱 한 번만 실행되도록 보장해줌

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        // 토큰이 유효하면 SecurityContextHolder에 인증 정보를 등록: "이 요청은 인증된 사용자다"라고 Spring Security에 알리는 방법
        if (token != null && jwtTokenProvider.isValid(token)) {
            String email = jwtTokenProvider.getEmail(token);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 토큰이 없거나 무효해도 예외를 던지지 않고 그냥 통과시킴: 인증이 필요한지 아닌지는 다음 단계(SecurityConfig)에서 판단하게 위임
        filterChain.doFilter(request, response);
    }

    // Authorization: Bearer {토큰} 형식에서 토큰 문자열만 뽑아냄
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
