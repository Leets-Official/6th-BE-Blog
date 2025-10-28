package com.leets.backend.blog.post.repository;

import com.leets.backend.blog.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}