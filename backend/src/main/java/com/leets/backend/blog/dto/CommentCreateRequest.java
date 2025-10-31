package com.leets.backend.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentCreateRequest(
        @NotBlank(message = "내용은 비어 있을 수 없습니다.")
        @Size(max = 500, message = "내용은 500자 이하여야 합니다.")
        String content
) {}
