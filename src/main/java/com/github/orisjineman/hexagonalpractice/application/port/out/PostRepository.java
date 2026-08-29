package com.github.orisjineman.hexagonalpractice.application.port.out;

import com.github.orisjineman.hexagonalpractice.domain.Post;

public interface PostRepository {
    Post save(Post post);
}
