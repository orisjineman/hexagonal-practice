package com.github.orisjineman.hexagonalpractice.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    @Test
    void create_시_id는_null이고_email_password는_그대로_들어간다() {
        // given
        String email = "test@test.com";
        String password = "encoded-password-123";

        // when
        User user = User.create(email, password);

        // then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
    }
}
