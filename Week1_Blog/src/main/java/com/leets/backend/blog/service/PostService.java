package com.leets.backend.blog.service;

import com.leets.backend.blog.entity.*;
import com.leets.backend.blog.dto.request.*;
import com.leets.backend.blog.dto.response.*;
import com.leets.backend.blog.exception.*;
import com.leets.backend.blog.repository.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    // 전체 게시물 조회
    public List<PostResponseDto> getAllPosts(Long userId, int page) {

        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Post> postsPage = postRepository.findAll(pageable); // 페이지네이션 적용
        //List<Post> posts = postRepository.findAll();

        return postsPage.stream().map(post -> {
            User user = userRepository.findById(post.getUser().getUserId())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            return new PostResponseDto(post, user, 1L); // 3번째 파라미터 임시 userId
        }).toList();
    }

    public PostDetailResponseDTO createPost(PostRequestDTO dto, Long currentUserId){
        Post newPost = Post.createPost(dto, userRepository.findById(1L).get());

        Post savedPost = postRepository.save(newPost);

        return getPostDetail(savedPost.getPostId(), 1L); // 1L userId
    }

    public PostDetailResponseDTO getPostDetail(Long postId, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        User user = post.getUser();

        List<CommentResponseDTO> comments = post.getComments().stream()
                .map(comment -> new CommentResponseDTO(comment, comment.getUser(), 1L)).toList();
        PostDetailResponseDTO dto = PostDetailResponseDTO.toDto(post, user, comments, 1L);

        return dto;
    }

    public PostDetailResponseDTO updatePost(Long postId, PostRequestDTO dto, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getUserId().equals(1L))
            throw new CustomException(ErrorCode.NO_UPDATE_PERMISSION);

        post.updatePost(dto.getTitle(), dto.getContent());

        Post updatedPost = postRepository.save(post);

        return getPostDetail(postId, 1L);
    }

    public PostDetailResponseDTO deletePost(Long postId, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getUserId().equals(1L)) {
            throw new CustomException(ErrorCode.NO_DELETE_PERMISSION);
        }

        postRepository.delete(post);

        return getPostDetail(postId, 1L);
    }

    public CommentResponseDTO createComment(Long postId, CommentRequestDTO dto , Long currentUserId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        User user = userRepository.findById(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Comment newComment = Comment.createComment(post, user, dto.getContent());

        Comment savedComment = commentRepository.save(newComment);

        return new CommentResponseDTO(savedComment, savedComment.getUser(), 1L);
    }

    public CommentResponseDTO createChildComment(Long postId, Long parentCommentId, CommentRequestDTO dto , Long currentUserId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        User user = userRepository.findById(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Comment newComment = Comment.createChildComment(post, user, parentCommentId, dto.getContent());

        Comment savedComment = commentRepository.save(newComment);

        return new CommentResponseDTO(savedComment, savedComment.getUser(), 1L);
    }

    public CommentResponseDTO updateComment(Long commentId, CommentRequestDTO dto, Long currentUserId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getUserId().equals(1L))
            throw new CustomException(ErrorCode.NO_UPDATE_PERMISSION);

        comment.updateComment(dto.getContent());

        Comment updatedComment = commentRepository.save(comment);

        return new CommentResponseDTO(updatedComment, updatedComment.getUser(), 1L);
    }

    public CommentResponseDTO deleteComment(Long commentId, Long currentUserId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getUserId().equals(1L))
            throw new CustomException(ErrorCode.NO_DELETE_PERMISSION);

        commentRepository.delete(comment);

        return new CommentResponseDTO(comment, comment.getUser(), 1L);
    }
}
