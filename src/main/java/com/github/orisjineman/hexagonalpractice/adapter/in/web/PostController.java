package com.github.orisjineman.hexagonalpractice.adapter.in.web;

import com.github.orisjineman.hexagonalpractice.application.port.in.CreatePostUseCase;
import com.github.orisjineman.hexagonalpractice.domain.Post;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final CreatePostUseCase createPostUseCase;

    public PostController(CreatePostUseCase createPostUseCase) {
        this.createPostUseCase = createPostUseCase;
    }

    @PostMapping
    public PostResponse createPost(@RequestBody CreatePostRequest request) {
        Post post = createPostUseCase.createPost(request.title(), request.content());
        return new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getCreatedAt());
    }
}
