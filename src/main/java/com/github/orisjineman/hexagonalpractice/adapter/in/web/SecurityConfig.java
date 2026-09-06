package com.github.orisjineman.hexagonalpractice.adapter.in.web;

import com.github.orisjineman.hexagonalpractice.adapter.out.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 보호는 세션 기반 인증(쿠키)에서 중요한 개념인데,
                // JWT는 매 요청마다 토큰을 헤더에 직접 실어보내는 방식이라 CSRF 공격 자체가 성립하기 어려움.
                // 그래서 JWT 방식에선 관례적으로 꺼둠.
                .csrf(csrf -> csrf.disable())
                // STATELESS: "서버는 세션을 만들지도, 유지하지도 않겠다"는 선언.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 회원가입/로그인은 토큰이 있을 수가 없으니 인증 없이 허용
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        // 그 외 모든 요청(/api/posts/** 포함)은 인증 필요
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(frame -> frame.disable())) // H2 콘솔 접근 허용용
                // JwtAuthenticationFilter를 Spring Security 기본 필터 체인의 특정 위치 이전에 끼워넣는 것
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
