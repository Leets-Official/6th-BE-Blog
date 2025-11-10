package com.leets.backend.blog.user.repository;

import com.leets.backend.blog.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);     // 이메일 존재 여부
    boolean existsByNickname(String nickname); // 닉네임 존재 여부
}
