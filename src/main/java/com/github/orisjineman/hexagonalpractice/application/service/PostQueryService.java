package com.github.orisjineman.hexagonalpractice.application.service;

import com.github.orisjineman.hexagonalpractice.application.exception.PostNotFoundException;
import com.github.orisjineman.hexagonalpractice.application.port.in.GetPostListUseCase;
import com.github.orisjineman.hexagonalpractice.application.port.in.GetPostUseCase;
import com.github.orisjineman.hexagonalpractice.application.port.in.SearchPostUseCase;
import com.github.orisjineman.hexagonalpractice.application.port.out.PostRepository;
import com.github.orisjineman.hexagonalpractice.domain.Post;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostQueryService implements GetPostUseCase, GetPostListUseCase, SearchPostUseCase {
    // "쓰기"랑 "읽기" 책임을 분리

    private final PostRepository postRepository;

    public PostQueryService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));
    }

    @Override
    public List<Post> getPostList() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> searchByTitle(String keyword) {
        return postRepository.findByTitleContaining(keyword);
    }
}
