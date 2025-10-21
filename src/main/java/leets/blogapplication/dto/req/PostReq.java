package leets.blogapplication.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class PostReq {
    Long id;

    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 30, message = "제목은 100자 이하만 허용됩니다.")
    String title;

    @NotBlank(message = "내용은 필수입니다.")
    String content;

    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
