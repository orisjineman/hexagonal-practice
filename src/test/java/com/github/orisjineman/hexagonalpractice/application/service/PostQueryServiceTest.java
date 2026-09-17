package com.github.orisjineman.hexagonalpractice.application.service;

import com.github.orisjineman.hexagonalpractice.application.exception.PostNotFoundException;
import com.github.orisjineman.hexagonalpractice.application.port.out.PostRepository;
import com.github.orisjineman.hexagonalpractice.domain.Post;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostQueryServiceTest {

    @Mock
    private PostRepository postRepository;

    @Test
    void getPost_존재하는_id로_조회하면_Post를_반환한다() {
        // given
        PostQueryService service = new PostQueryService(postRepository);
        Post post = new Post("post-id-1", "첫 글", "내용", LocalDateTime.now());
        when(postRepository.findById("post-id-1")).thenReturn(Optional.of(post));

        // when
        Post result = service.getPost("post-id-1");

        // then
        assertThat(result).isEqualTo(post);
    }

    @Test
    void getPost_존재하지_않는_id로_조회하면_예외를_던진다() {
        // given
        PostQueryService service = new PostQueryService(postRepository);
        when(postRepository.findById("no-such-id")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> service.getPost("no-such-id"))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("no-such-id");
    }
}
