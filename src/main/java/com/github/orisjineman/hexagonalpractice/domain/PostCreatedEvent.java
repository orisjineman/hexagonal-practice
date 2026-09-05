package com.github.orisjineman.hexagonalpractice.domain;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostCreatedEvent {

    private final Long postId;
    private final String title;
    private final LocalDateTime occurredAt;

    public PostCreatedEvent(Long postId, String title) {
        this.postId = postId;
        this.title = title;
        this.occurredAt = LocalDateTime.now();
    }
}
