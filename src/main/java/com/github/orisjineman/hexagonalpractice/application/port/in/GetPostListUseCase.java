package com.github.orisjineman.hexagonalpractice.application.port.in;

import com.github.orisjineman.hexagonalpractice.domain.Post;

import java.util.List;

public interface GetPostListUseCase {
    List<Post> getPostList();
    // 굳이 두 개로 나눈 이유: "단건 조회"랑 "목록 조회"는 서로 다른 유스케이스라서.
    // 하나로 합쳐도 되지만(PostQueryUseCase 이런 식으로),
    // 인터페이스를 잘게 쪼개는 게 헥사고날/DDD에서 권장하는 방식 (인터페이스 분리 원칙, ISP)
}
