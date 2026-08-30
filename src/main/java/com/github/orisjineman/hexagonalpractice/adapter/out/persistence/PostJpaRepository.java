package com.github.orisjineman.hexagonalpractice.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<PostJpaEntity, Long>, PostJpaCustomRepository {
    // JpaRepository 기본 기능 + Custom(QueryDSL) 기능 합쳐짐
}
