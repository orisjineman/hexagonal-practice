package com.github.orisjineman.hexagonalpractice.application.port.out;

public interface TokenProvider {
    String createToken(String email);
}
