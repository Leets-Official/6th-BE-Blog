package com.leets.backend.blog_manage.repository;

import com.leets.backend.blog_manage.model.Post;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class PostRepository {

    // DB 역할을 할 HashMap
    private static final Map<Long, Post> store = new HashMap<>();
    // ID를 1씩 증가시키기 위한 변수
    private static long sequence = 0L;

    // 게시글 저장
    public Post save(Post post) {
        post.setId(++sequence);
        store.put(post.getId(), post);
        return post;
    }

    // 게시글 전체 조회
    public List<Post> findAll() {
        return new ArrayList<>(store.values());
    }
}