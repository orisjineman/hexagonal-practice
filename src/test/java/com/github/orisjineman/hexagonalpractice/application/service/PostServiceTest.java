package com.github.orisjineman.hexagonalpractice.application.service;

import com.github.orisjineman.hexagonalpractice.application.port.out.PostRepository;
import com.github.orisjineman.hexagonalpractice.application.port.out.UserRepository;
import com.github.orisjineman.hexagonalpractice.domain.Post;
import com.github.orisjineman.hexagonalpractice.domain.PostCreatedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Test
    void createPost_호출시_저장하고_이벤트를_발행하며_저장된_Post를_반환한다() {
        // given
        PostService postService = new PostService(postRepository, applicationEventPublisher);

        Post savedPost = new Post("saved-id-123", "첫 글", "헥사고날 연습중", LocalDateTime.now());

        // postRepository.save()가 어떤 Post 객체든 인자로 받으면, savedPost를 리턴하도록 가짜로 동작
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        // when
        Post result = postService.createPost("첫 글", "헥사고날 연습중");

        // then
        assertThat(result).isEqualTo(savedPost);

        verify(postRepository).save(any(Post.class));

        ArgumentCaptor<PostCreatedEvent> eventCaptor = ArgumentCaptor.forClass(PostCreatedEvent.class);
        verify(applicationEventPublisher).publishEvent(eventCaptor.capture());
        assertThat(eventCaptor.getValue().getPostId()).isEqualTo("saved-id-123");
        assertThat(eventCaptor.getValue().getTitle()).isEqualTo("첫 글");
    }
}
