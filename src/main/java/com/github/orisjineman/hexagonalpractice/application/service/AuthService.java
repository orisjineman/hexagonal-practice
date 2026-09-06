package com.github.orisjineman.hexagonalpractice.application.service;

import com.github.orisjineman.hexagonalpractice.application.exception.InvalidCredentialsException;
import com.github.orisjineman.hexagonalpractice.application.port.in.LoginUseCase;
import com.github.orisjineman.hexagonalpractice.application.port.out.TokenProvider;
import com.github.orisjineman.hexagonalpractice.application.port.out.UserRepository;
import com.github.orisjineman.hexagonalpractice.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    // AuthService는 TokenProvider라는 인터페이스만 안다.
    // 실제로 그게 JWT인지, 나중에 다른 토큰 방식(예: Paseto)으로 바뀌는지 전혀 몰라도 된다.
    private final TokenProvider tokenProvider;

    public  AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public String login(String email, String rawPassword) {
        //이메일이 없거나 비밀번호가 틀려도 동일하게 InvalidCredentialsException을 던지는 것은 의도적이다.
        // "이메일은 맞는데 비밀번호가 틀렸다"는 식으로 세분화해서 알려주면, 공격자가 "이 이메일은 가입되어 있구나"를 유추할 수 있는 보안 허점이 생긴다.
        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return tokenProvider.createToken(user.getEmail());
    }
}
