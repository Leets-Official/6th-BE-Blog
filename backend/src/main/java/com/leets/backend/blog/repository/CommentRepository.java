package com.leets.backend.blog.repository;

import org.hibernate.annotations.Comments;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentRepository extends JpaRepository<Comments, Integer> {
}
