package com.github.orisjineman.hexagonalpractice.application.port.in;

import com.github.orisjineman.hexagonalpractice.domain.User;

public interface SignUpUseCase {
    User signUp(String email, String rawPassword);
}
