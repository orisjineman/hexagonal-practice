package com.github.orisjineman.hexagonalpractice.application.service;

import com.github.orisjineman.hexagonalpractice.application.exception.InvalidCredentialsException;
import com.github.orisjineman.hexagonalpractice.application.port.out.TokenProvider;
import com.github.orisjineman.hexagonalpractice.application.port.out.UserRepository;
import com.github.orisjineman.hexagonalpractice.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenProvider tokenProvider;

    @Test
    void login_이메일과_비밀번호가_맞으면_토큰을_반환한다() {
        // given
        AuthService authService = new AuthService(userRepository, passwordEncoder, tokenProvider);
        User user = User.create("test@test.com", "encoded-password");
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("raw-password", "encoded-password")).thenReturn(true);
        when(tokenProvider.createToken("test@test.com")).thenReturn("jwt-token-value");

        // when
        String token = authService.login("test@test.com", "raw-password");

        // then
        assertThat(token).isEqualTo("jwt-token-value");
    }

    @Test
    void login_존재하지_않는_이메일이면_예외를_던진다() {
        // given
        AuthService authService = new AuthService(userRepository, passwordEncoder, tokenProvider);
        when(userRepository.findByEmail("no-such@test.com")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authService.login("no-such@test.com", "raw-password"))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void login_비밀번호가_틀리면_예외를_던진다() {
        // given
        AuthService authService = new AuthService(userRepository, passwordEncoder, tokenProvider);
        User user = User.create("test@test.com", "encoded-password");
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong-password", "encoded-password")).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> authService.login("test@test.com", "wrong-password"))
                .isInstanceOf(InvalidCredentialsException.class);
    }
}
