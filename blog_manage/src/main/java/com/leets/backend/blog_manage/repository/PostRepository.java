package com.leets.backend.blog_manage.repository;

import com.leets.backend.blog_manage.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 게시물 리포지토리
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}