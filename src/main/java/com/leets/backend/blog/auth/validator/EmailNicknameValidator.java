package com.leets.backend.blog.auth.validator;

import com.leets.backend.blog.common.exception.CustomException;
import com.leets.backend.blog.common.exception.ErrorCode;
import com.leets.backend.blog.user.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class EmailNicknameValidator {
    private final UserRepository userRepository;

    public EmailNicknameValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 이메일 중복 체크
    public void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL, "이미 존재하는 이메일입니다.");
        }
    }

    // 닉네임 중복 체크
    public void validateNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.DUPLICATED_NICKNAME, "이미 존재하는 닉네임입니다.");
        }
    }
}
