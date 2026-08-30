package com.github.orisjineman.hexagonalpractice.application.port.in;

import com.github.orisjineman.hexagonalpractice.domain.Post;

public interface GetPostUseCase {
    Post getPost(Long id);
}
