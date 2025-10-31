package com.leets.backend.blog.controller;

import com.leets.backend.blog.dto.CommentCreateRequest;
import com.leets.backend.blog.dto.CommentResponseDto;
import com.leets.backend.blog.dto.CommentUpdateRequest;
import com.leets.backend.blog.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "댓글", description = "댓글 CRUD API")
@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "댓글 조회", description = "비로그인 사용자도 조회 가능합니다.", security = {})
    @GetMapping
    public List<CommentResponseDto> list(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    @Operation(summary = "댓글 작성", description = "사용자는 특정 게시물에 댓글을 작성합니다.", security = { @SecurityRequirement(name = "BearerAuth") })
    @PostMapping
    public CommentResponseDto create(@PathVariable Long postId,
                                     @Valid @RequestBody CommentCreateRequest req,
                                     @AuthenticationPrincipal Long userId) {
        return commentService.createComment(postId, req, userId);
    }

    @Operation(summary = "댓글 수정", description = "사용자는 자신이 작성한 댓글을 수정가능합니다.", security = { @SecurityRequirement(name = "BearerAuth") })
    @PutMapping("/{commentId}")
    public CommentResponseDto update(@PathVariable Long postId,
                                     @PathVariable Long commentId,
                                     @Valid @RequestBody CommentUpdateRequest req,
                                     @AuthenticationPrincipal Long userId) {
        return commentService.updateComment(commentId, req, userId);
    }

    @Operation(summary = "댓글 삭제", description = "사용자는 자신이 작성한 댓글을 삭제가능합니다.", security = { @SecurityRequirement(name = "BearerAuth") })
    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable Long postId,
                       @PathVariable Long commentId,
                       @AuthenticationPrincipal Long userId) {
        commentService.deleteComment(commentId, userId);
    }
}
