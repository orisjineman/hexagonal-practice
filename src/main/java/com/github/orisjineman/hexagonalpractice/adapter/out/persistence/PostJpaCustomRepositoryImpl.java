package com.github.orisjineman.hexagonalpractice.adapter.out.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.github.orisjineman.hexagonalpractice.adapter.out.persistence.QPostJpaEntity.postJpaEntity;

@Repository
public class PostJpaCustomRepositoryImpl implements PostJpaCustomRepository {

    private final JPAQueryFactory queryFactory;

    public PostJpaCustomRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<PostJpaEntity> findByTitleContaining(String keyword) {
        return queryFactory
                .selectFrom(postJpaEntity)
                .where(postJpaEntity.title.contains(keyword))
                .fetch();
    }
}
