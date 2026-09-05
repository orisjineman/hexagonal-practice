package com.github.orisjineman.hexagonalpractice.application.service;

import com.github.orisjineman.hexagonalpractice.domain.PostCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class PostCreatedEventListener {

    private final static Logger LOG = LoggerFactory.getLogger(PostCreatedEventListener.class);

    @Async("eventTaskExecutor")
    @EventListener
    public void handle(PostCreatedEvent event) {
        LOG.info("[스레드: {}] 게시글 생성 이벤트 수신 - id: {}, title: {}, 발생시각: {}",
                Thread.currentThread().getName(), event.getPostId(), event.getTitle(), event.getOccurredAt());
    }
}
