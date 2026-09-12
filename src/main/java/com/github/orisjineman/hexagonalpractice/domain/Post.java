package com.github.orisjineman.hexagonalpractice.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Post {
    private String id;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    // 순수 자바 객체 - JPA 몰라도 됨, 프레임워크 의존성 없음
    public Post(String id, String title, String content, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static Post create(String title, String content) {
        return new Post(null, title, content, LocalDateTime.now());
    }
}
