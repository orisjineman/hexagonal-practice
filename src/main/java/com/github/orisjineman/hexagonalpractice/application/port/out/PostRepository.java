package com.github.orisjineman.hexagonalpractice.application.port.out;

import com.github.orisjineman.hexagonalpractice.domain.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    Post save(Post post);

    Optional<Post> findById(String id);

    List<Post> findAll();

    List<Post> findByTitleContaining(String keyword);
}
