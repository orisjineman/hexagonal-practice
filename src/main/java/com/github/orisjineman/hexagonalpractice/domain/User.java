package com.github.orisjineman.hexagonalpractice.domain;

import lombok.Getter;

@Getter
public class User {

    private Long id;
    private String email;
    private String password;    // 암호화된 비밀번호가 들어감 (평문 아님)

    public User(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public static User create(String email, String encodedPassword) {
        // encodedPassword: 도메인은 "어떻게 암호화하는지" 몰라도 됨.
        return new User(null, email, encodedPassword);
    }
}
