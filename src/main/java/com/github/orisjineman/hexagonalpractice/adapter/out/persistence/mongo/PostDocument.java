package com.github.orisjineman.hexagonalpractice.adapter.out.persistence.mongo;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Document("posts")
public class PostDocument {

    @Id
    private String id;  // MongoDB는 기본적으로 String(ObjectId) 사용 — JPA의 Long id와 다름!

    private String title;
    private String content;
    private LocalDateTime createdAt;

    protected PostDocument() {
        // MongoDB 드라이버도 리플렉션으로 객체를 만들기 때문에 기본 생성자 필요
    }

    public PostDocument(String id, String title, String content, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }
}
