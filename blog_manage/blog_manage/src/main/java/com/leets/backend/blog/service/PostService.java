package com.leets.backend.blog.service;

import com.leets.backend.blog.entity.Post;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.dto.PostCreateRequest;
import com.leets.backend.blog.dto.PostUpdateRequest;
import com.leets.backend.blog.exception.PostNotFoundException;
import com.leets.backend.blog.exception.UserAccessDeniedException;
import com.leets.backend.blog.repository.PostRepository;
import com.leets.backend.blog.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Post createPost(PostCreateRequest request, User user) {
        Post post = new Post(user, request.getTitle(), request.getContent());
        return postRepository.save(post);
    }

    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    public Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
    }

    @Transactional
    public Post updatePost(Long postId, PostUpdateRequest request, User user) {
        Post post = findPostById(postId);
        if (!Objects.equals(post.getUser().getUserId(), user.getUserId())) {
            throw new UserAccessDeniedException("이 글을 수정할 권한이 없습니다. 작성자만 수정할 수 있습니다.");
        }
        post.update(request.getTitle(), request.getContent());
        return post;
    }

    @Transactional
    public void deletePost(Long postId, User user) {
        Post post = findPostById(postId);
        if (!Objects.equals(post.getUser().getUserId(), user.getUserId())) {
            throw new UserAccessDeniedException("이 글을 삭제할 권한이 없습니다. 작성자만 삭제할 수 있습니다.");
        }
        postRepository.delete(post);
    }
}