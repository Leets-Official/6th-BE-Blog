package com.leets.backend.blog.repository;


import com.leets.backend.blog.model.Post;

import java.util.*;

public class PostRepository {

    private final Map<Long, Post> store = new HashMap<>();
    private Long sequenceId = 0L;


    // 게시글 작성
    public Post save(Post post) {
        if(post.getId() == null) {
            post.setId(++sequenceId);
        }
        store.put(post.getId(), post);
        return post;
    }

    // 전체 글 조회
    public List<Post> findAll() {
        return new ArrayList<>(store.values());
    }

    // 상세 글 조회
    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }
}
