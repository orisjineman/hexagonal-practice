package com.github.orisjineman.hexagonalpractice.application.service;

import com.github.orisjineman.hexagonalpractice.application.port.in.GetPostListUseCase;
import com.github.orisjineman.hexagonalpractice.application.port.in.GetPostUseCase;
import com.github.orisjineman.hexagonalpractice.application.port.out.PostRepository;
import com.github.orisjineman.hexagonalpractice.domain.Post;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostQueryService implements GetPostUseCase, GetPostListUseCase {
    // "쓰기"랑 "읽기" 책임을 분리

    private final PostRepository postRepository;

    public PostQueryService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id=" + id));
            // 추후 예외처리 단계에서 커스텀 예외로 바꿀거임
    }

    @Override
    public List<Post> getPostList() {
        return postRepository.findAll();
    }
}
