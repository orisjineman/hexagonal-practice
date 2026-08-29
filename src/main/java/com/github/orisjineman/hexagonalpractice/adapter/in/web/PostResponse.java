package com.github.orisjineman.hexagonalpractice.adapter.in.web;

import java.time.LocalDateTime;

public record PostResponse(Long id, String title, String content, LocalDateTime createdAt) { }
