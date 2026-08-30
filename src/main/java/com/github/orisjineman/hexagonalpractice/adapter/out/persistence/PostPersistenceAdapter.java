package com.github.orisjineman.hexagonalpractice.adapter.out.persistence;

import com.github.orisjineman.hexagonalpractice.application.port.out.PostRepository;
import com.github.orisjineman.hexagonalpractice.domain.Post;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PostPersistenceAdapter implements PostRepository {

    private final PostJpaRepository postJpaRepository;  // 진짜 JPA 기술

    public PostPersistenceAdapter(PostJpaRepository postJpaRepository) {
        this.postJpaRepository = postJpaRepository;
    }

    @Override
    public Post save(Post post) {
        // 도메인 Post -> JPA Entity 변환
        PostJpaEntity entity = new PostJpaEntity(
                post.getId(), post.getTitle(), post.getContent(), post.getCreatedAt()
        );

        PostJpaEntity saved = postJpaRepository.save(entity);

        // JPA Entity -> 도메인 Post 변환해서 리턴
        return new Post(saved.getId(), saved.getTitle(), saved.getContent(), saved.getCreatedAt());
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postJpaRepository.findById(id)
                // JPA Entity -> 도메인 Post 변환해서 리턴
                .map(entity -> new Post(entity.getId(), entity.getTitle(), entity.getContent(), entity.getCreatedAt()));
    }

    @Override
    public List<Post> findAll() {
        return postJpaRepository.findAll().stream()
                // JPA Entity -> 도메인 Post 변환해서 리턴
                .map(entity -> new Post(entity.getId(), entity.getTitle(), entity.getContent(), entity.getCreatedAt()))
                .toList();
    }
}
