package com.github.orisjineman.hexagonalpractice.adapter.out.persistence.mongo;

import com.github.orisjineman.hexagonalpractice.application.port.out.PostRepository;
import com.github.orisjineman.hexagonalpractice.domain.Post;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PostMongoPersistenceAdapter implements PostRepository {

    private final PostMongoRepository postMongoRepository;

    public PostMongoPersistenceAdapter(PostMongoRepository postMongoRepository) {
        this.postMongoRepository = postMongoRepository;
    }

    @Override
    public Post save(Post post) {
        PostDocument document = new PostDocument(post.getId(), post.getTitle(), post.getContent(), post.getCreatedAt());
        PostDocument saved = postMongoRepository.save(document);
        return new Post(saved.getId(), saved.getTitle(), saved.getContent(), saved.getCreatedAt());
    }

    @Override
    public Optional<Post> findById(String id) {
        return postMongoRepository.findById(id)
                .map(doc -> new Post(doc.getId(), doc.getTitle(), doc.getContent(), doc.getCreatedAt()));
    }

    @Override
    public List<Post> findAll() {
        return postMongoRepository.findAll().stream()
                .map(doc -> new Post(doc.getId(), doc.getTitle(), doc.getContent(), doc.getCreatedAt()))
                .toList();
    }

    @Override
    public List<Post> findByTitleContaining(String keyword) {
        return postMongoRepository.findByTitleContaining(keyword).stream()
                .map(doc -> new Post(doc.getId(), doc.getTitle(), doc.getContent(), doc.getCreatedAt()))
                .toList();
    }
}
