package com.leets.backend.blog_manage.service;

import com.leets.backend.blog_manage.model.Post;
import com.leets.backend.blog_manage.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired // 의존성 주입
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 게시글 생성
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    // 게시글 목록 조회
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
}