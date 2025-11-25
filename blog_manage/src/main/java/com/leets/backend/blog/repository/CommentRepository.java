package com.leets.backend.blog.repository;

import com.leets.backend.blog.entity.Comment;
import com.leets.backend.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
