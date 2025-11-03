package leets.blogapplication.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import leets.blogapplication.domain.Post;
import leets.blogapplication.dto.req.PostReq;
import leets.blogapplication.dto.res.PostRes;
import leets.blogapplication.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping
public class PostController {

    private final PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 전체 조회
    @GetMapping("/posts")
    public ResponseEntity<List<PostRes>> getPosts(@RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                                  @RequestParam(required = false, defaultValue = "createdAt", value = "criteria") String criteria) {
        List<PostRes> body = postService.getAll(pageNo, criteria);
        //criteria: 정렬기준 (작성일, 댓글순 등등)
        return ResponseEntity.ok(body);
    }

    // 단건 조회 (id)
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostRes> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getById(id));
    }

    // 제목 검색 (?title=)
    @GetMapping("/posts/{title}")
    public ResponseEntity<List<PostRes>> getPostByTitle(@PathVariable String title,
                                                        @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                                        @RequestParam(required = false, defaultValue = "createdAt", value = "criteria") String criteria
                                                        ) {
        List<PostRes> body = postService.getByTitle(title, pageNo, criteria);
        return ResponseEntity.ok(body);
    }

    // c
    @PostMapping("/post")
    public ResponseEntity<Void> createPost(@RequestBody PostReq req) {
        try {
            postService.create(req);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // u
    @PutMapping("/post/{id}")
    public ResponseEntity<Void> updatePost(@RequestBody PostReq req) {
        postService.update(req);
        return ResponseEntity.noContent().build();
    }

    //d
    @DeleteMapping("/post/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }
}