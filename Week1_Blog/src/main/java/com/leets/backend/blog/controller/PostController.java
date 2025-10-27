package com.leets.backend.blog.controller;

import com.leets.backend.blog.DTO.*;
import com.leets.backend.blog.service.PostService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PostController {
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    // 게시물 전체 리스트 조회
    @GetMapping("/posts")
    public List<PostResponseDto> GetPosts(HttpSession session) {
        return service.GetAllPosts((Long) session.getAttribute("UserId"));
    }
    
    // 게시물 상세 보기
    @GetMapping("/post/{id}")
    public PostDetailResponseDTO GetPostDetail(@PathVariable("id") Long postId, HttpSession session){
        return service.GetPostDetail(postId, (Long) session.getAttribute("UserId"));
    }

    // 게시물 생성
    @PostMapping("/post")
    public PostDetailResponseDTO CreatePost(@Valid @RequestBody PostRequestDTO dto, HttpSession session){
        return service.CreatePost(dto, (Long) session.getAttribute("UserId"));
    }

    // 게시물 수정
    @PutMapping("/post/{id}")
    public PostDetailResponseDTO UpdatePost(
            @PathVariable("id") Long postId,
            @Valid @RequestBody PostRequestDTO dto,
            HttpSession session){
        return service.UpdatePost(postId, dto, (Long) session.getAttribute("UserId"));
    }

    // 게시물 삭제
    @DeleteMapping("/post/{id}")
    public PostDetailResponseDTO  DeletePost(@PathVariable("id") Long postId, HttpSession session){
        return service.DeletePost(postId, (Long) session.getAttribute("UserId"));
    }

    // 댓글 생성
    @PostMapping("/post/{id}/comment")
    public CommentResponseDTO CreateComment(
            @PathVariable("id") Long postId,
            @Valid @RequestBody CommentRequestDTO dto,
            HttpSession session){
        return service.CreateComment(postId, dto, (Long) session.getAttribute("UserId"));
    }

    // 대댓글 생성
    @PostMapping("/post/{postId}/comment/{commentId}")
    public CommentResponseDTO CreateChildComment(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody CommentRequestDTO dto,
            HttpSession session){
        return service.CreateChildComment(postId, commentId, dto, (Long) session.getAttribute("UserId"));
    }

    // 댓글 수정
    @PutMapping("/comment/{id}")
    public CommentResponseDTO UpdateComment(
            @PathVariable("id") Long postId,
            @Valid @RequestBody CommentRequestDTO dto,
            HttpSession session){
        return service.UpdateComment(postId, dto, (Long) session.getAttribute("UserId"));
    }

    // 댓글 삭제
    @DeleteMapping
    public CommentResponseDTO DeleteComment(
            @PathVariable("id") Long postId,
            HttpSession session){
        return service.DeleteComment(postId, (Long) session.getAttribute("UserId"));
    }
}
