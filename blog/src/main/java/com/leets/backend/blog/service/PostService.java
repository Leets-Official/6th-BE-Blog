package com.leets.backend.blog.service;

import com.leets.backend.blog.dto.*;
import com.leets.backend.blog.entity.Post;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.exception.PostNotFoundException;
import com.leets.backend.blog.exception.auth.AuthException;
import com.leets.backend.blog.exception.auth.ErrorCode;
import com.leets.backend.blog.repository.PostRepository;
import com.leets.backend.blog.repository.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private static final int PAGE_SIZE = 10;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 게시물 생성
    public PostResponseDTO createPost(PostCreateRequestDTO requestDTO, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException(ErrorCode.USER_NOT_FOUND));

        Post post = Post.createPost(
                requestDTO.getTitle(), requestDTO.getContent(), user
        );
        Post savedPost = postRepository.save(post);

        return PostResponseDTO.from(savedPost);
    }

    // 게시물 상세 조회
    @Transactional(readOnly = true)
    public PostDetailResponseDTO getPostDetail(Long postId, Principal principal) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        // Principal에서 email을 추출하고 DB에서 User 조회
        Optional<String> currentUserEmail = Optional.ofNullable(principal).map(Principal::getName);

        User loginUser = currentUserEmail.flatMap(userRepository::findByEmail)
                .orElse(null);

        return PostDetailResponseDTO.from(post, loginUser);
    }

    // 게시물 목록 조회
    @Transactional(readOnly = true)
    public List<PostListResponseDTO> getPostList(int page) {

        // 1. Pageable 객체 생성 (page는 0부터 시작, 정렬 기준: createdAt 내림차순)
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "createdAt"));

        // 2. DTO 프로젝션을 사용한 메서드 호출
        Page<PostListResponseDTO> postPage = postRepository.findAllAsListDTO(pageable);

        // 3. Page<DTO>에서 List<DTO>를 반환
        return postPage.getContent();
    }

    // 게시물 수정
    public PostResponseDTO updatePost(Long postId, PostUpdateRequestDTO requestDTO, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        checkPostAuthor(post, user);

        post.updatePost(requestDTO.getTitle(), requestDTO.getContent());

        return PostResponseDTO.from(post);
    }

    // 게시물 삭제
    public void deletePost(Long postId, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        checkPostAuthor(post, user);

        postRepository.delete(post);
    }

    private void checkPostAuthor(Post post, User user) {
        if (!post.getUser().getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("게시물 수정/삭제 권한이 없습니다.");
        }
    }
}
