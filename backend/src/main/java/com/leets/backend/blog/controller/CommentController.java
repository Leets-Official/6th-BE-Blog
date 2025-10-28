package com.leets.backend.blog.controller;

import com.leets.backend.blog.dto.CommentRequestDTO;
import com.leets.backend.blog.dto.CommentResponseDTO;
import com.leets.backend.blog.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    // 댓글 조회 (비로그인 가능)
    @Operation(summary = "댓글 조회", description = "비로그인 사용자도 조회 가능합니다.", security = {})
    @GetMapping
    public List<CommentResponseDTO> getComments(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    // 댓글 작성
    @Operation(summary = "댓글 작성", description = "사용자는 특정 게시물에 댓글을 작성합니다.", security = { @SecurityRequirement(name = "BearerAuth") })
    @PostMapping
    public CommentResponseDTO createComment(@PathVariable Long postId,
                                            @RequestBody CommentRequestDTO dto) {
        // 임시로 Mock 사용
        Long mockUserId = 1L;
        return commentService.createComment(postId, dto, mockUserId);
    }

    // 댓글 수정
    @Operation(summary = "댓글 수정", description = "사용자는 자신이 작성한 댓글을 수정가능합니다.", security = { @SecurityRequirement(name = "BearerAuth") })
    @PutMapping("/{commentId}")
    public CommentResponseDTO updateComment(@PathVariable Long postId,
                                            @PathVariable Long commentId,
                                            @RequestBody CommentRequestDTO dto) {
        Long mockUserId = 1L;
        return commentService.updateComment(commentId, dto, mockUserId);
    }

    // 댓글 삭제
    @Operation(summary = "댓글 삭제", description = "사용자는 자신이 작성한 댓글을 삭제가능합니다.", security = { @SecurityRequirement(name = "BearerAuth") })
    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long postId,
                              @PathVariable Long commentId) {
        Long mockUserId = 1L;
        commentService.deleteComment(commentId, mockUserId);
    }
}
