package com.leets.backend.blog.service;

import com.leets.backend.blog.dto.CommentCreateRequestDTO;
import com.leets.backend.blog.dto.CommentResponseDTO;
import com.leets.backend.blog.dto.CommentUpdateRequestDTO;
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
    
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private static final String DUMMY_USER_EMAIL = "dummy@naver.com";
    
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }
    
    // 댓글 생성
    public CommentResponseDTO createComment(Long postId, CommentCreateRequestDTO requestDTO) {
        User user = findDummyUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());

        Comment comment = Comment.createComment(requestDTO.getContent(), user, post);
        Comment savedComment = commentRepository.save(comment);

        return CommentResponseDTO.from(savedComment);
    }
    
    // 댓글 수정
    public CommentResponseDTO updateComment(Long postId, Long commentId, CommentUpdateRequestDTO requestDTO) {
        User user = findDummyUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException());

        // 해당 게시글의 댓글이 맞는지 점검
        if(!comment.getPost().equals(post)){
            throw new CommentNotFoundException(commentId, postId);
        }

        // 권한 확인
        checkCommentAuthor(comment, user);

        // 댓글 수정
        comment.updateComment(requestDTO.getContent());

        return CommentResponseDTO.from(comment);
    }
    
    // 댓글 삭제
    public void deleteComment(Long postId, Long commentId) {
        User user = findDummyUser();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException());

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException());

        // 해당 게시글의 댓글이 맞는지 점검
        if(!comment.getPost().equals(post)){
            throw new CommentNotFoundException(commentId, postId);
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

    // 더미 유저 찾기
    private User findDummyUser() {
        User user = userRepository.findByEmail(DUMMY_USER_EMAIL);
        if(user == null) {
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
