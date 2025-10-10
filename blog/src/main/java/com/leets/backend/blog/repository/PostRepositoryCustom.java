package com.leets.backend.blog.repository;

import com.leets.backend.blog.entity.Post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> findPostsWithPaging(int offset, int limit);

    long countAllPosts();
}
