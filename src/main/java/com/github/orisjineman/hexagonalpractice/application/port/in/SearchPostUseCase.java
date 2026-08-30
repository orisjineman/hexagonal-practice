package com.github.orisjineman.hexagonalpractice.application.port.in;

import com.github.orisjineman.hexagonalpractice.domain.Post;

import java.util.List;

public interface SearchPostUseCase {
    List<Post> searchByTitle(String keyword);
}
