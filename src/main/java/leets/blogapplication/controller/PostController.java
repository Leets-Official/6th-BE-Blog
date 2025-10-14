package leets.blogapplication.controller;

import leets.blogapplication.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping
public class PostController {

    private final PostService postService;
    public PostController(PostService postService) { this.postService = postService; }

    // r

    // 전체 조회
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getPosts() {
        List<Post> body = postService.getAll().stream()
                .map(Post::transform)
                .toList();
        return ResponseEntity.ok(body);
    }

    // 단건 조회 (id)
    @GetMapping("/post/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(Post.transform(postService.getById(id)));
    }

    // 제목 검색 (?title=)
    @GetMapping("/post")
    public ResponseEntity<List<Post>> getPostByTitle(@RequestParam String title) {
        List<Post> body = postService.getByTitle(title).stream()
                .map(Post::transform)
                .toList();
        return ResponseEntity.ok(body);
    }

    // c
    @PostMapping("/post")
    public ResponseEntity<Long> createPost(@RequestBody PostIn req) {
        Long id = postService.create(req.id(), req.title(), req.content());
        return ResponseEntity.ok(id);
    }

    // u
    @PutMapping("/post/{id}")
    public ResponseEntity<Void> updatePost(@PathVariable Long id, @RequestBody PostIn req) {
        postService.update(id, req.title(), req.content());
        return ResponseEntity.noContent().build();
    }

    //d
    @DeleteMapping("/post/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //req
    public record PostIn(Long id, String title, String content) {}

    // response
    public record Post(
            Long id,
            String title,
            String content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        public static Post transform(leets.blogapplication.domain.Post p) {
            return new Post(p.getId(), p.getTitle(), p.getContent(), p.getCreatedAt(), p.getUpdatedAt());
        }
    }
}