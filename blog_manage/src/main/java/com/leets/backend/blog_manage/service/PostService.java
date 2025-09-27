package com.leets.backend.blog_manage.service;

import com.leets.backend.blog_manage.model.Post;
import com.leets.backend.blog_manage.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections; // 추가
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired // 의존성 주입
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return Collections.emptyList();
    }
}