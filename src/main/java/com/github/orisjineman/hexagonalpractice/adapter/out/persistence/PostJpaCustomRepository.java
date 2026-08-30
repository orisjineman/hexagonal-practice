package com.github.orisjineman.hexagonalpractice.adapter.out.persistence;

import java.util.List;

public interface PostJpaCustomRepository {
    List<PostJpaEntity> findByTitleContaining(String keyword);
}
