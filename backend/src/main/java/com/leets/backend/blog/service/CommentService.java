package com.leets.backend.blog.service;

import com.leets.backend.blog.entity.Comment;
import com.leets.backend.blog.entity.Post;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.dto.CommentRequestDTO;
import com.leets.backend.blog.dto.CommentResponseDTO;
import com.leets.backend.blog.repository.CommentRepository;
import com.leets.backend.blog.repository.PostRepository;
import com.leets.backend.blog.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 명시적 생성자 주입
    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository,
                          UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 댓글 작성 - Create
    public CommentResponseDTO createComment(Long postId, CommentRequestDTO dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(dto.getContent());
        comment.setCreatedAt(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);
        return new CommentResponseDTO(saved);
    }

    // 게시글별 댓글 조회 - Read (로그인을 하지 않고도 조회 가능)
    public List<CommentResponseDTO> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPost_PostId(postId);
        return comments.stream()
                .map(CommentResponseDTO::new)
                .collect(Collectors.toList());
    }

    // 댓글 수정 - Update (본인만 가능)
    public CommentResponseDTO updateComment(Long commentId, CommentRequestDTO dto, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("본인 댓글만 수정할 수 있습니다.");
        }

        comment.setContent(dto.getContent());
        comment.setUpdatedAt(LocalDateTime.now());

        Comment updated = commentRepository.save(comment);
        return new CommentResponseDTO(updated);
    }

    // 댓글 삭제 - Delete (본인만 가능)
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("본인 댓글만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }
}