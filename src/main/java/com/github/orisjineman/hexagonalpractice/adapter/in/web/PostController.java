package com.github.orisjineman.hexagonalpractice.adapter.in.web;

import com.github.orisjineman.hexagonalpractice.application.port.in.CreatePostUseCase;
import com.github.orisjineman.hexagonalpractice.application.port.in.GetPostListUseCase;
import com.github.orisjineman.hexagonalpractice.application.port.in.GetPostUseCase;
import com.github.orisjineman.hexagonalpractice.domain.Post;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final CreatePostUseCase createPostUseCase;
    private final GetPostUseCase getPostUseCase;
    private final GetPostListUseCase getPostListUseCase;

    public PostController(CreatePostUseCase createPostUseCase,
                          GetPostUseCase getPostUseCase,
                          GetPostListUseCase getPostListUseCase) {
        this.createPostUseCase = createPostUseCase;
        this.getPostUseCase = getPostUseCase;
        this.getPostListUseCase = getPostListUseCase;
    }

    @PostMapping
    public PostResponse createPost(@RequestBody CreatePostRequest request) {
        Post post = createPostUseCase.createPost(request.title(), request.content());
        return PostWebMapper.toResponse(post);
    }

    @GetMapping("/{id}")
    public PostResponse getPost(@PathVariable Long id) {
        Post post = getPostUseCase.getPost(id);
        return PostWebMapper.toResponse(post);
    }

    @GetMapping
    public List<PostResponse> getPostList() {
        return getPostListUseCase.getPostList().stream()
                .map(PostWebMapper::toResponse)
                .toList();
    }
}
