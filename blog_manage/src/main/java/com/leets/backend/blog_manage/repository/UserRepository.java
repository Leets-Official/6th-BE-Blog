package com.leets.backend.blog_manage.repository;

import com.leets.backend.blog_manage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    // 카카오 ID로 사용자를 찾기 위한 메소드 추가
    Optional<User> findByKakaoId(String kakaoId);
}