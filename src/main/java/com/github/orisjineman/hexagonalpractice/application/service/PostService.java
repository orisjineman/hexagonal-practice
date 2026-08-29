package com.github.orisjineman.hexagonalpractice.application.service;

import com.github.orisjineman.hexagonalpractice.application.port.in.CreatePostUseCase;
import com.github.orisjineman.hexagonalpractice.application.port.out.PostRepository;
import com.github.orisjineman.hexagonalpractice.domain.Post;
import org.springframework.stereotype.Service;

@Service
public class PostService implements CreatePostUseCase {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post createPost(String title, String content) {
        Post post = Post.create(title, content);
        return postRepository.save(post);
    }
}
