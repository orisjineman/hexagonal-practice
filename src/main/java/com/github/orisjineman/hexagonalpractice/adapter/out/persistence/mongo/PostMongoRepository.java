package com.github.orisjineman.hexagonalpractice.adapter.out.persistence.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostMongoRepository extends MongoRepository<PostDocument, String> {
    List<PostDocument> findByTitleContaining(String keyword);
}
