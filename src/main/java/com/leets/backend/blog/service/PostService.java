package com.leets.backend.blog.service;

import com.leets.backend.blog.domain.Post;
import com.leets.backend.blog.domain.User;
import com.leets.backend.blog.dto.PostDetailResponse;
import com.leets.backend.blog.dto.PostListResponse;
import com.leets.backend.blog.dto.PostSaveRequest;
import com.leets.backend.blog.repository.PostRepository;
import com.leets.backend.blog.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    //게시물 생성
    @Transactional
    public PostDetailResponse createPost(Long userId, PostSaveRequest request) {
        User writer = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 유저를 찾을 수 없습니다. id=" + userId));

        Post newPost = new Post();
        newPost.setTitle(request.getTitle());
        newPost.setContent(request.getContent());
        newPost.setUser(writer);

        Post savedPost = postRepository.save(newPost);

        return new PostDetailResponse(savedPost, true);
    }
    
    // 모든 게시물 목록조회
    @Transactional(readOnly = true)
    public List<PostListResponse> findAllPosts() {
        return postRepository.findAll().stream()
                .map(PostListResponse::new)
                .collect(Collectors.toList());
    }

    //특정 게시물
    @Transactional(readOnly = true)
    public PostDetailResponse findPostById(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시물을 찾을 수 없습니다. id=" + postId));

        boolean isOwner = post.getUser().getId().equals(userId);

        return new PostDetailResponse(post, isOwner);
    }

    //수정
    @Transactional
    public PostDetailResponse updatePost(Long userId, Long postId, PostSaveRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시물을 찾을 수 없습니다. id=" + postId));

        if (!post.getUser().getId().equals(userId)) {
            throw new SecurityException("게시물을 수정할 권한이 없습니다.");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());

        return new PostDetailResponse(post, true);
    }

    //삭제
    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시물을 찾을 수 없습니다. id=" + postId));

        if (!post.getUser().getId().equals(userId)) {
            throw new SecurityException("게시물을 삭제할 권한이 없습니다.");
        }

        postRepository.delete(post);
    }
}