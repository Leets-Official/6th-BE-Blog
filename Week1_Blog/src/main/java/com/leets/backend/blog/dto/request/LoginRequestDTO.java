package com.leets.backend.blog.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public class LoginRequestDTO {
    @Email
    private String email;
    @Size(min = 8, max = 20)
    @NotBlank
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
