package com.leets.backend.blog.service;

import com.leets.backend.blog.dto.UserLoginRequest;
import com.leets.backend.blog.dto.UserSignUpRequest;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.exception.BadCredentialsException;
import com.leets.backend.blog.exception.DuplicateEmailException;
import com.leets.backend.blog.exception.DuplicateNicknameException;
import com.leets.backend.blog.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User signUp(UserSignUpRequest request) {
        // 1. 이메일 중복 확인
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateEmailException();
        }

        // 2. 닉네임 중복 확인
        if (userRepository.findByNickname(request.getNickname()).isPresent()) {
            throw new DuplicateNicknameException();
        }

        // 3. 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 4. User 엔티티 생성 및 저장
        User newUser = new User(
                request.getEmail(),
                encodedPassword,
                request.getNickname(),
                "EMAIL" // 로그인 타입은 일단 EMAIL로 고정
        );

        return userRepository.save(newUser);
    }

    @Transactional(readOnly = true)
    public User login(UserLoginRequest request) {
        // 1. 이메일로 사용자 찾기
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(BadCredentialsException::new);

        // 2. 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException();
        }

        return user;
    }
}
