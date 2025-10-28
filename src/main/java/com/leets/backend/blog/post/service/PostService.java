package com.leets.backend.blog.post.service;

import com.leets.backend.blog.common.exception.CustomException;
import com.leets.backend.blog.common.exception.ErrorCode;
import com.leets.backend.blog.post.controller.dto.request.PostRequest;
import com.leets.backend.blog.post.controller.dto.request.PostUpdateRequest;
import com.leets.backend.blog.post.controller.dto.response.PostResponse;
import com.leets.backend.blog.post.controller.dto.response.PostSummaryResponse;
import com.leets.backend.blog.post.entity.Post;
import com.leets.backend.blog.post.exception.PostNotFoundException;
import com.leets.backend.blog.post.entity.User;
import com.leets.backend.blog.post.repository.PostRepository;
import com.leets.backend.blog.post.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 게시글 전체 조회
    @Transactional(readOnly = true)
    public List<PostSummaryResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(PostSummaryResponse::from)
                .toList();
    }

    // 게시글 상세 조회
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("해당 게시글이 존재하지 않습니다."));
        return PostResponse.from(post);
    }

    // 게시글 생성
    public PostResponse createPost(PostRequest postRequest) {
        User user = userRepository.findByEmail("test@example.com")
                .orElseGet(() -> userRepository.save(new User("test@example.com", "1234", "더미유저")));

        Post post = Post.of(postRequest.getTitle(), postRequest.getContent(), user);
        Post saved = postRepository.save(post);

        return PostResponse.from(saved);
    }

    // 게시글 수정
    public PostResponse updatePost(Long postId, Long userId, PostUpdateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));

        // 회원 기능 생기면 삭제, 임시 기능
        if (userId == 0) {
            User dummy = new User("test@example.com", "1234", "더미유저");
            dummy = userRepository.save(dummy);
            userId = dummy.getId();
        }

        if (!post.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NO_UPDATE, "게시글 수정 권한이 없습니다.");
        }

        post.update(request.getTitle(), request.getContent());
        return PostResponse.from(postRepository.save(post));
    }

    // 게시글 삭제
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NO_DELETE, "게시글 삭제 권한이 없습니다.");
        }

        postRepository.delete(post);
    }
}
