package com.leets.backend.blog.repository;

import com.leets.backend.blog.model.Post;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PostRepository {
    private final Map<Long, Post> store = new HashMap<>();
    private Long currentId = 1L; // id를 직접 증가시켜서 관리

    public List<Post> findAll() {
        return new ArrayList<>(store.values());
    }

    public Post save(Post post) {
        if (post.getId() == null) { // 새 글이면 id 생성
            post.setId(currentId++);
        }
        store.put(post.getId(), post);
        return post;
    }
}
