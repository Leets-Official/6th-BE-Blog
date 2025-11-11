package com.leets.backend.blog.repository;

import com.leets.backend.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByKakaoId(String kakaoId);
    boolean existsUserByEmail(String email);
    boolean existsUserByKakaoId(String kakaoId);
    boolean existsUserByNickname(String nickname);
}
