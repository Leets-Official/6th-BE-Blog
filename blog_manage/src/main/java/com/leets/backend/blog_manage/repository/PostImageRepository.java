package com.leets.backend.blog_manage.repository;

import com.leets.backend.blog_manage.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 게시물 이미지 리포지토리
 */
@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}