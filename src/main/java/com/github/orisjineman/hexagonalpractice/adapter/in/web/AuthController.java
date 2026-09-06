package com.github.orisjineman.hexagonalpractice.adapter.in.web;

import com.github.orisjineman.hexagonalpractice.adapter.out.persistence.UserJpaRepository;
import com.github.orisjineman.hexagonalpractice.application.port.in.LoginUseCase;
import com.github.orisjineman.hexagonalpractice.application.port.in.SignUpUseCase;
import com.github.orisjineman.hexagonalpractice.domain.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final SignUpUseCase signUpUseCase;
    private final LoginUseCase loginUseCase;

    public AuthController(SignUpUseCase signUpUseCase, LoginUseCase loginUseCase) {
        this.signUpUseCase = signUpUseCase;
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse signUp (@RequestBody SignUpRequest request) {
        User user = signUpUseCase.signUp(request.email(), request.password());
        return UserWebMapper.toResponse(user);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        String token = loginUseCase.login(request.email(), request.password());
        return new LoginResponse(token);
    }
}
