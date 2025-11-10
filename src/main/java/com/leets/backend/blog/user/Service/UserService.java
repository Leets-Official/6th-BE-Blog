package com.leets.backend.blog.user.service;

import com.leets.backend.blog.common.exception.CustomException;
import com.leets.backend.blog.common.exception.ErrorCode;
import com.leets.backend.blog.user.controller.dto.response.UserResponse;
import com.leets.backend.blog.user.entity.User;
import com.leets.backend.blog.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 사용자 조회
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다."));

        return new UserResponse(user.getEmail(), user.getNickname());
    }

    // 닉네임 변경
    public UserResponse updateNickname(Long id, String nickname) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다."));

        user.updateNickname(nickname);
        userRepository.save(user);

        return new UserResponse(user.getEmail(), user.getNickname());
    }

    // 비밀번호 수정 (PasswordEncoder 포함)
    public void updatePassword(Long id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "유저를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_INVALID_PASSWORD, "비밀번호가 일치하지 않습니다.");
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.updatePassword(encodedNewPassword);

        userRepository.save(user);
    }
}