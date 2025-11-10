package com.leets.backend.blog.auth.service;

import com.leets.backend.blog.auth.controller.dto.request.SignupRequest;
import com.leets.backend.blog.auth.controller.dto.response.SignupResponse;
import com.leets.backend.blog.auth.validator.EmailNicknameValidator;
import com.leets.backend.blog.user.entity.User;
import com.leets.backend.blog.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailNicknameValidator validator;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       EmailNicknameValidator validator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.validator = validator;
    }

    // 회원가입 로직
    public SignupResponse signup(SignupRequest signupRequest) {
        validator.validateEmail(signupRequest.getEmail());
        validator.validateNickname(signupRequest.getNickname());

        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        User user = new User(signupRequest.getEmail(), encodedPassword, signupRequest.getNickname());
        User saved = userRepository.save(user);

        return SignupResponse.from(saved);
    }
}
