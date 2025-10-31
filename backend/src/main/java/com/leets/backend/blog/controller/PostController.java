package com.leets.backend.blog.controller;

import com.leets.backend.blog.dto.PostRequestDTO;
import com.leets.backend.blog.dto.PostResponseDTO;
import com.leets.backend.blog.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시물", description = "게시글 CRUD API")
@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "게시글 생성", description = "사용자는 제목과 내용을 작성하여 게시글을 생성합니다.")
    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@Valid @RequestBody PostRequestDTO dto) {
        PostResponseDTO response = postService.createPost(dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시글 전체 조회", description = "생성된 게시글을 조회할 수 있습니다.")
    @GetMapping
    public Page<PostResponseDTO> list(@ParameterObject @PageableDefault(
            size = 10, sort = "createdAt", direction = Sort.Direction.DESC
    ) Pageable pageable) {
        return postService.getPosts(pageable);
    }

    @Operation(summary = "게시글 상세 조회", description = "특정 게시글을 상세 조회하여 게시글의 내용을 볼 수 있습니다.")
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        PostResponseDTO response = postService.getPostById(id);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "게시글 수정", description = "사용자는 작성한 게시글의 제목과 내용을 수정가능합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostRequestDTO dto
    ) {
        PostResponseDTO response = postService.updatePost(id, dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }
}
