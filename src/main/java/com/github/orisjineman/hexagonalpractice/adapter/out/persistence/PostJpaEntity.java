package com.github.orisjineman.hexagonalpractice.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
public class PostJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private LocalDateTime createdAt;

    protected PostJpaEntity() {
        // JPA는 기본 생성자가 필수(리플렉션으로 객체 생성함)
    }

    public PostJpaEntity(Long id, String title, String content, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }
}
