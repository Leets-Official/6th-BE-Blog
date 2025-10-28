package com.leets.backend.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository; 

import com.leets.backend.blog.domain.Comment;
import com.leets.backend.blog.domain.Post; 

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByPost(Post post);
}