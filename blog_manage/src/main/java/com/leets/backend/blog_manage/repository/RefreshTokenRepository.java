package com.leets.backend.blog_manage.repository;

import com.leets.backend.blog_manage.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}