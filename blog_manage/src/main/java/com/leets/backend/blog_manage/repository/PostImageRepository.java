package com.leets.backend.blog_manage.repository;

import com.leets.backend.blog_manage.model.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}