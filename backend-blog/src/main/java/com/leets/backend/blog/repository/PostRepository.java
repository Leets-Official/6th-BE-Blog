package com.leets.backend.blog.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.leets.backend.blog.model.Post;

@Repository
public class PostRepository {
    // 메모리 기반 저장을 위한 HashMap
    private static final Map<Long, Post> store = new HashMap<>();
    // id 생성을 위한 AtomicLong (동시성 문제 방지)
    private static final AtomicLong sequence = new AtomicLong(0);

    /**
     * 새 게시글 저장
     */
    public Post save(Post post) {
        // 새 ID를 생성하고 Post 객체에 설정
        post.setId(sequence.incrementAndGet());
        // HashMap에 저장
        store.put(post.getId(), post);
        return post;
    }

    /**
     * 모든 게시글 조회
     */
    public List<Post> findAll() {
        // HashMap에 저장된 모든 Post를 리스트로 변환하여 반환
        return new ArrayList<>(store.values());
    }
}
