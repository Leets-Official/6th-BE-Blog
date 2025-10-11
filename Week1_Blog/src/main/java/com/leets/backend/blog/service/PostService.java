package com.leets.backend.blog.service;

import com.leets.backend.blog.DTO.*;
import com.leets.backend.blog.domain.*;
import com.leets.backend.blog.repository.*;
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
    public List<PostResponseDto> GetAllPosts(Long userId) {
        List<Post> posts = postRepository.findAll();

        return posts.stream().map(post -> {
            User user = userRepository.findById(post.getUser().getUserId())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
            return new PostResponseDto(post, user, 1L); // 3번째 파라미터 임시 userId
        }).toList();
    }

    public PostDetailResponseDTO CreatePost(PostRequestDTO dto, Long currentUserId){
        Post newPost = Post.createPost(dto, userRepository.findById(1L).get());

        Post savedPost = postRepository.save(newPost);

        return GetPostDetail(savedPost.getPostId(), 1L); // 1L userId
    }

    public PostDetailResponseDTO GetPostDetail(Long postId, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시물입니다."));

        User user = post.getUser();

        List<CommentResponseDTO> comments = post.getComments().stream()
                .map(comment -> new CommentResponseDTO(comment, comment.getUser(), 1L)).toList();
        PostDetailResponseDTO dto = new PostDetailResponseDTO(post, user, comments, 1L);

        return dto;
    }

    public PostDetailResponseDTO UpdatePost(Long postId, PostRequestDTO dto, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시물입니다."));

        if (!post.getUser().getUserId().equals(1L))
            throw new RuntimeException("수정 권한이 없습니다. post_id : " + postId);

        post.updatePost(dto.getTitle(), dto.getContent());

        Post updatedPost = postRepository.save(post);

        return GetPostDetail(postId, 1L);
    }

    public PostDetailResponseDTO DeletePost(Long postId, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("삭제할 게시글이 존재하지 않습니다."));

        if (!post.getUser().getUserId().equals(1L)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }

        postRepository.delete(post);

        return GetPostDetail(postId, 1L);
    }

    public CommentResponseDTO CreateComment(Long postId, CommentRequestDTO dto , Long currentUserId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        Comment newComment = Comment.createComment(post, user, dto);

        Comment savedComment = commentRepository.save(newComment);

        return new CommentResponseDTO(savedComment, savedComment.getUser(), 1L);
    }

    public CommentResponseDTO CreateChildComment(Long postId, Long parentCommentId, CommentRequestDTO dto , Long currentUserId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));

        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        Comment newComment = Comment.createChildComment(post, user, parentCommentId, dto);

        Comment savedComment = commentRepository.save(newComment);

        return new CommentResponseDTO(savedComment, savedComment.getUser(), 1L);
    }

    public CommentResponseDTO UpdateComment(Long commentId, CommentRequestDTO dto, Long currentUserId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 댓글입니다."));

        if (!comment.getUser().getUserId().equals(1L))
            throw new RuntimeException("수정 권한이 없습니다. comment_id : " + commentId);

        comment.updateComment(dto.getContent());

        Comment updatedComment = commentRepository.save(comment);

        return new CommentResponseDTO(updatedComment, updatedComment.getUser(), 1L);
    }

    public CommentResponseDTO DeleteComment(Long commentId, Long currentUserId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 댓글입니다."));

        if (!comment.getUser().getUserId().equals(1L))
            throw new RuntimeException("삭제 권한이 없습니다. comment_id : " + commentId);

        commentRepository.delete(comment);

        return new CommentResponseDTO(comment, comment.getUser(), 1L);
    }
}
