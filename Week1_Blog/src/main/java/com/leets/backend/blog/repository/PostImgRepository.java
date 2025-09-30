package com.leets.backend.blog.repository;


import com.leets.backend.blog.domain.Post;
import com.leets.backend.blog.domain.PostImg;
import com.leets.backend.blog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImgRepository extends JpaRepository<PostImg, Long>  {
}
