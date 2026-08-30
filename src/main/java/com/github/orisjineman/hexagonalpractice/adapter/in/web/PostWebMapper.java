package com.github.orisjineman.hexagonalpractice.adapter.in.web;

import com.github.orisjineman.hexagonalpractice.domain.Post;

// 매퍼는 adapter.in.web에 있음 (application이나 domain에 있는 게 아님)
// 왜냐면 "도메인 객체를 HTTP 응답 형태로 어떻게 표현할지"는 순전히 웹 어댑터의 관심사라서.
// 도메인은 자기가 HTTP로 어떻게 보여질지 전혀 몰라야 함.
public class PostWebMapper {

    public static PostResponse toResponse(Post post) {
        return new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getCreatedAt());
    }
}
