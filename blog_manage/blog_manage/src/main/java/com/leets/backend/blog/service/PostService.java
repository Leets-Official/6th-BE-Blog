package com.leets.backend.blog.service;

import com.leets.backend.blog.entity.Post;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.dto.PostCreateRequest;
import com.leets.backend.blog.dto.PostUpdateRequest;
import com.leets.backend.blog.exception.PostNotFoundException;
import com.leets.backend.blog.repository.PostRepository;
import com.leets.backend.blog.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)     // 이 클래스의 메서드들은 기본적으로 데이터를 읽기만 함
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional      // 해당 메서드는 readOnly가 아님을 알려줌, 메서드 실패 시 Rollback
    public Post createPost(PostCreateRequest request) {
        // ToDo: 인증 기능 추가 후 실제 사용자 정보로 변경해야 함
        // 임시로 ID가 1인 사용자가 모든 작업 수행한다고 가정
        User temporaryUser = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("임시 사용자를 찾을 수 없습니다."));

        Post post = new Post(temporaryUser, request.getTitle(), request.getContent());
        return postRepository.save(post);
    }

    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    public Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

    @Transactional
    public Post updatePost(Long postId, PostUpdateRequest request) {
        Post post = findPostById(postId);

        // TODO: 사용자 인증 기능 추가 후, 게시글 작성자 본인인지 확인하는 로직 필요

        post.update(request.getTitle(), request.getContent());
        return post;
    }

    @Transactional
    public void deletePost(Long postId) {
        Post post = findPostById(postId);

        // TODO: 사용자 인증 기능 추가 후, 게시글 작성자 본인인지 확인하는 로직 필요

        postRepository.delete(post);
    }
}