package com.github.orisjineman.hexagonalpractice.application.exception;

public class PostNotFoundException extends RuntimeException {
    // RuntimeException을 상속하는 이유: Checked Exception(예: Exception 직접 상속)으로 만들면
    // 모든 메서드 시그니처에 throws를 달아야 해서 인터페이스(Port)까지 오염됨.
    // 그래서 이런 도메인 예외는 관례적으로 Unchecked(RuntimeException)로 만든다.

    public PostNotFoundException(String id) {
        super("게시글을 찾을 수 없습니다. id=" + id);
    }
}
