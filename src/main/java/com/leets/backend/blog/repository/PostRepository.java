package com.leets.backend.blog.repository;

import com.leets.backend.blog.controller.dto.PostResponse;
import com.leets.backend.blog.model.Post;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PostRepository {
    private final Map<Long, Post> store = new HashMap<>();
    private Long sequence = 0L;

    // 모든 글 조회
    public List<Post> findAll(){
        return new ArrayList<>(store.values());
    }

    // 새 글 저장
    public Post save(Post post) {
        post.setId(++sequence); //ID 자동 증가
        store.put(post.getId(), post);
        return post;
    }
}
