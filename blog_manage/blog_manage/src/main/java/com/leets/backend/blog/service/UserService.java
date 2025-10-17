package com.leets.backend.blog.service;

import com.leets.backend.blog.dto.UserUpdateRequest;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.exception.DuplicateNicknameException;
import com.leets.backend.blog.exception.UserNotFoundException;
import com.leets.backend.blog.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public User updateUser(Long userId, UserUpdateRequest request) {
        User user = getUserById(userId);

        // TODO: 현재 로그인한 사용자와 userId가 일치하는지 확인하는 로직 필요

        // 닉네임 중복 확인 (변경 요청이 있고, 기존 닉네임과 다를 경우)
        if (request.getNickname() != null && !request.getNickname().isEmpty() && !user.getNickname().equals(request.getNickname())) {
            if (userRepository.findByNickname(request.getNickname()).isPresent()) {
                throw new DuplicateNicknameException();
            }
        }

        // 비밀번호 암호화 (변경 요청이 있을 경우)
        String encodedPassword = null;
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            encodedPassword = passwordEncoder.encode(request.getPassword());
        }

        user.update(
                request.getNickname(),
                encodedPassword,
                request.getProfileUrl(),
                LocalDateTime.now()
        );

        return userRepository.save(user);
    }
}
