package com.github.orisjineman.hexagonalpractice.application.service;

import com.github.orisjineman.hexagonalpractice.application.port.in.CreatePostUseCase;
import com.github.orisjineman.hexagonalpractice.application.port.out.PostRepository;
import com.github.orisjineman.hexagonalpractice.domain.Post;
import com.github.orisjineman.hexagonalpractice.domain.PostCreatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class PostService implements CreatePostUseCase {

    private final PostRepository postRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PostService(PostRepository postRepository,  ApplicationEventPublisher eventPublisher) {
        this.postRepository = postRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Post createPost(String title, String content) {
        Post post = Post.create(title, content);
        Post saved = postRepository.save(post);

        eventPublisher.publishEvent(new PostCreatedEvent(saved.getId(), saved.getTitle()));

        return saved;
    }
}
