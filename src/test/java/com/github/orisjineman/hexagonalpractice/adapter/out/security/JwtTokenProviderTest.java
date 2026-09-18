package com.github.orisjineman.hexagonalpractice.adapter.out.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtTokenProviderTest {

    private static final String SECRET = "this-is-a-very-long-secret-key-for-jwt-practice-project-1234";

    @Test
    void createToken_으로_만든_토큰에서_getEmail로_이메일을_꺼낼_수_있다() {
        // given
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET, 1000L * 60 * 60);

        // when
        String token = jwtTokenProvider.createToken("test@test.com");
        String email = jwtTokenProvider.getEmail(token);

        // then
        assertThat(email).isEqualTo("test@test.com");
    }

    @Test
    void createToken_으로_만든_토큰은_isValid가_true다() {
        // given
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET, 1000L * 60 * 60);

        // when
        String token = jwtTokenProvider.createToken("test@test.com");

        // then
        assertThat(jwtTokenProvider.isValid(token)).isTrue();
    }

    @Test
    void 다른_시크릿키로_만든_토큰은_isValid가_false다() {
        // given
        JwtTokenProvider originalProvider = new JwtTokenProvider(SECRET, 1000L * 60 * 60);
        JwtTokenProvider differentSecretProvider = new JwtTokenProvider("this-is-a-completely-different-secret-key-1234567890", 1000L);

        // when
        String token = differentSecretProvider.createToken("test@test.com");

        // then
        assertThat(originalProvider.isValid(token)).isFalse();
    }

    @Test
    void 이미_만료된_토큰은_isValid가_false다() {
        // given
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(SECRET, -1000L);

        // when
        String token = jwtTokenProvider.createToken("test@test.com");

        // then
        assertThat(jwtTokenProvider.isValid(token)).isFalse();
    }
}
