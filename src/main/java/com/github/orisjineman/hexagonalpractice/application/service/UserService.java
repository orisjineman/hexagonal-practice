package com.github.orisjineman.hexagonalpractice.application.service;

import com.github.orisjineman.hexagonalpractice.application.exception.DuplicateEmailException;
import com.github.orisjineman.hexagonalpractice.application.port.in.SignUpUseCase;
import com.github.orisjineman.hexagonalpractice.application.port.out.UserRepository;
import com.github.orisjineman.hexagonalpractice.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements SignUpUseCase {

    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User signUp(String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException(email);
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = User.create(email, encodedPassword);
        return userRepository.save(user);
    }
}
