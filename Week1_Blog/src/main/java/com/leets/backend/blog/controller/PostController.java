package com.leets.backend.blog.controller;

import com.leets.backend.blog.dto.request.CommentRequestDTO;
import com.leets.backend.blog.dto.request.PostRequestDTO;
import com.leets.backend.blog.dto.response.CommentResponseDTO;
import com.leets.backend.blog.dto.response.PostDetailResponseDTO;
import com.leets.backend.blog.dto.response.PostResponseDto;
import com.leets.backend.blog.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Post", description = "게시물 관리")
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    // 게시물 전체 리스트 조회
    @Operation(
            summary = "게시물 전체 리스트 조회(페이지네이션 적용)",
            description = "조회할 게시물 페이지 번호를 쿼리스트링으로 받음"
    )
    @GetMapping
    public List<PostResponseDto> getPosts(
            HttpSession session,
            @RequestParam(required = false, defaultValue = "1") int page) {
        return service.getAllPosts((Long) session.getAttribute("UserId"), page);
    }
    
    // 게시물 상세 보기
    @Operation(
            summary = "게시물 상세 조회",
            description = "게시물에 대한 제목, 내용, 댓글 등 조회."
    )
    @GetMapping("/detail/{id}")
    public PostDetailResponseDTO getPostDetail(@PathVariable("id") Long postId, HttpSession session){
        return service.getPostDetail(postId, (Long) session.getAttribute("UserId"));
    }

    // 게시물 생성
    @Operation(
            summary = "게시물 생성",
            description = "새로운 게시물을 작성"
    )
    @PostMapping
    public PostDetailResponseDTO createPost(@Valid @RequestBody PostRequestDTO dto, HttpSession session){
        return service.createPost(dto, (Long) session.getAttribute("UserId"));
    }

    // 게시물 수정
    @Operation(
            summary = "게시물 수정",
            description = "게시물에 대한 제목, 내용등을 수정"
    )
    @PutMapping("/{id}")
    public PostDetailResponseDTO updatePost(
            @PathVariable("id") Long postId,
            @Valid @RequestBody PostRequestDTO dto,
            HttpSession session){
        return service.updatePost(postId, dto, (Long) session.getAttribute("UserId"));
    }

    // 게시물 삭제
    @Operation(
            summary = "게시물 삭제",
            description = "게시물을 삭제함"
    )
    @DeleteMapping("/{id}")
    public PostDetailResponseDTO  deletePost(@PathVariable("id") Long postId, HttpSession session){
        return service.deletePost(postId, (Long) session.getAttribute("UserId"));
    }

    // 댓글 생성
    @Operation(
            summary = "댓글 생성",
            description = "신규 댓글을 생성함"
    )
    @PostMapping("/{id}/comment")
    public CommentResponseDTO createComment(
            @PathVariable("id") Long postId,
            @Valid @RequestBody CommentRequestDTO dto,
            HttpSession session){
        return service.createComment(postId, dto, (Long) session.getAttribute("UserId"));
    }

    // 대댓글 생성
    @Operation(
            summary = "대댓글 생성",
            description = "부모 댓글 id에 대한 자식 댓글 생성"
    )
    @PostMapping("/{postId}/comment/{commentId}")
    public CommentResponseDTO createChildComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody CommentRequestDTO dto,
            HttpSession session){
        return service.createChildComment(postId, commentId, dto, (Long) session.getAttribute("UserId"));
    }

    // 댓글 수정
    @Operation(
            summary = "댓글 수정",
            description = "댓글 내용을 수정함"
    )
    @PutMapping("/comment/{id}")
    public CommentResponseDTO updateComment(
            @PathVariable("id") Long postId,
            @Valid @RequestBody CommentRequestDTO dto,
            HttpSession session){
        return service.updateComment(postId, dto, (Long) session.getAttribute("UserId"));
    }

    // 댓글 삭제
    @Operation(
            summary = "댓글 삭제",
            description = "댓글을 삭제함"
    )
    @DeleteMapping("/comment/{id}")
    public CommentResponseDTO deleteComment(
            @PathVariable("id") Long postId,
            HttpSession session){
        return service.deleteComment(postId, (Long) session.getAttribute("UserId"));
    }
}
