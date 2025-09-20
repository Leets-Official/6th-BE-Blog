package com.leets.backend.blog.repository;

import com.leets.backend.blog.model.Post;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;

@Repository     // 데이터 저장/조회 관리 (HashMap 기반)
public class PostRepository {
    private final Map<Long, Post> posts = new HashMap<>();  // HashMap으로 저장, DB역할

    private Long sequence = 0L;     // 자동 증가 ID

    public Post save(Post post) {       // 게시글 저장 메서드
        post.setId(++sequence);
        posts.put(post.getId(), post);
        return post;
    }
    public Optional<Post> findById(Long id) {       // 단일 게시글 조회(Read) 메서드
        return Optional.ofNullable(posts.get(id));
    }

    public List<Post> findAll() {       // 전체 게시글 조회(Read) 메서드
        return new ArrayList<Post>(posts.values());
    }
}
