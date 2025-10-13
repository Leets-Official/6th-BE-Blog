package com.leets.backend.blog.service;

import com.leets.backend.blog.dto.PostRequestDTO;
import com.leets.backend.blog.dto.PostResponseDTO;
import com.leets.backend.blog.entity.Post;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.repository.PostRepository;
import com.leets.backend.blog.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public PostService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    // *임시* Mock 유저 생성
    private User getMockUser() {
        return userRepository.findById(1L).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail("test@test.com");
            newUser.setPassword("1234");
            newUser.setNickname("임시");
            newUser.setProvider("email");
            newUser.setCreatedAt(LocalDateTime.now());
            return userRepository.save(newUser);
        });
    }

    // 게시물 생성 - Create
    public PostResponseDTO createPost(PostRequestDTO dto) {
        User user = getMockUser();

        Post post = new Post();
        post.setUser(user);
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCreatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(post);

        return new PostResponseDTO(
                savedPost.getPostId(),
                savedPost.getTitle(),
                savedPost.getContent(),
                user.getNickname(),
                savedPost.getCreatedAt()
        );
    }

    // 게시물 전체 조회 - Read
    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(post -> new PostResponseDTO(
                        post.getPostId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getUser().getNickname(),
                        post.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    // 게시물 상세 조회 - Read
    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        return new PostResponseDTO(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getNickname(),
                post.getCreatedAt()
        );
    }

    // 게시물 수정 - Update
    public PostResponseDTO updatePost(Long id, PostRequestDTO dto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());

        Post updatedPost = postRepository.save(post);

        return new PostResponseDTO(
                updatedPost.getPostId(),
                updatedPost.getTitle(),
                updatedPost.getContent(),
                updatedPost.getUser().getNickname(),
                updatedPost.getCreatedAt()
        );
    }

    // 게시물 삭제 - Delete
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        postRepository.delete(post);
    }
}
