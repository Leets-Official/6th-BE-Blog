package com.leets.backend.blog.dto;

import jakarta.validation.constraints.NotBlank;

public class RefreshRequestDTO {
    @NotBlank
    private String refreshToken;

    // Getter
    public String getRefreshToken() { return refreshToken; }
}
