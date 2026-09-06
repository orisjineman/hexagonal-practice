package com.github.orisjineman.hexagonalpractice.application.port.in;

public interface LoginUseCase {
    String login(String username, String rawPassword);  // 로그인 성공 시 JWT 문자열 반환
}
