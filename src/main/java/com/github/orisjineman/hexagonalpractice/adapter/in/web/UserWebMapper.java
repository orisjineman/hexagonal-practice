package com.github.orisjineman.hexagonalpractice.adapter.in.web;

import com.github.orisjineman.hexagonalpractice.domain.User;

public class UserWebMapper {

    public static UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail());
    }
}
