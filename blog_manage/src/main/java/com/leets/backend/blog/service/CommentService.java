package com.leets.backend.blog.service;

import com.leets.backend.blog.dto.CommentCreateRequest;
import com.leets.backend.blog.dto.CommentResponse;
import com.leets.backend.blog.dto.CommentUpdateRequest;
import com.leets.backend.blog.entity.Comment;
import com.leets.backend.blog.entity.Post;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.exception.CommentAccessDeniedException;
import com.leets.backend.blog.exception.CommentNotFoundException;
import com.leets.backend.blog.exception.PostNotFoundException;
import com.leets.backend.blog.repository.CommentRepository;
import com.leets.backend.blog.repository.PostRepository;
import com.leets.backend.blog.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CommentService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private static final String DUMMY_USER_EMAIL = "dummy@naver.com";

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    //댓글 생성
    public CommentResponse createComment(Long postId, CommentCreateRequest request) {
        User user = findDummyUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        Comment comment = Comment.createComment(request.getContent(), user, post);
        commentRepository.save(comment);

        return CommentResponse.from(comment);
    }

    //댓글 수정
    public CommentResponse updateComment(Long postId, Long commentId, CommentUpdateRequest request) {
        User user = findDummyUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        if (!comment.getPost().getPostId().equals(post.getPostId())) {
            throw new CommentNotFoundException(commentId);
        }

        // 권한 확인
        checkCommentAuthor(comment, user);

        comment.updateComment(request.getContent());
        return CommentResponse.from(comment);
    }

    //댓글 삭제
    public void deleteComment(Long postId, Long commentId) {
        User user = findDummyUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        // (선택) 방어적 게시글 검증
        if (!comment.getPost().getPostId().equals(post.getPostId())) {
            throw new CommentNotFoundException(commentId);
        }

        // 권한 확인
        checkCommentAuthor(comment, user);

        commentRepository.delete(comment);
    }

    // 더미 유저 생성
    private User createDummyUser() {
        User user = new User();
        return userRepository.save(user.createDummy());
    }

    // 더미 유저 조회 (없으면 생성)
    private User findDummyUser() {
        User user = userRepository.findByEmail(DUMMY_USER_EMAIL);
        if (user == null) {
            user = createDummyUser();
        }
        return user;
    }

    // 댓글 작성자 권한 확인
    private void checkCommentAuthor(Comment comment, User user) {
        if (!comment.getUser().getUserId().equals(user.getUserId())) {
            throw new CommentAccessDeniedException();
        }
    }
}
