package com.leets.backend.blog_manage.repository;

import com.leets.backend.blog_manage.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 댓글 리포지토리
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}