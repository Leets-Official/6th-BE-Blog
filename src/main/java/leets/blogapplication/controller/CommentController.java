package leets.blogapplication.controller;

import leets.blogapplication.dto.req.CommentReq;
import leets.blogapplication.dto.res.CommentRes;
import leets.blogapplication.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comments")
    public ResponseEntity<Void> addComment(@RequestBody CommentReq req) {
        commentService.saveComment(req);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/posts/{postId}/comments") // post 기준으로 최상위 댓글 불러오기
    public ResponseEntity<List<CommentRes>> getComment(@PathVariable Long postId) {
        List<CommentRes> result = commentService.getComment(postId);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/comments/{commentId}/children") // 최상위 댓글 기준으로 이하 댓글 불러오기
    public ResponseEntity<List<CommentRes>> getChildComment(@PathVariable Long commentId) {
        List<CommentRes> result = commentService.getChildComment(commentId);
        return ResponseEntity.ok().body(result);
    }
}
