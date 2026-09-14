package com.github.orisjineman.hexagonalpractice.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PostTest {

    @Test
    void create시_id는_null이고_title_content는_그대로_들어간다() {
        // given
        String title = "첫 글";
        String content = "헥사고날 연습중";

        // when
        Post post = Post.create(title, content);

        // then
        assertThat(post.getId()).isNull();
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getCreatedAt()).isNotNull();
    }
}
