package com.leets.backend.blog.service;

import com.leets.backend.blog.config.JwtTokenProvider;
import com.leets.backend.blog.dto.UserLoginRequest;
import com.leets.backend.blog.dto.UserSignUpRequest;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.exception.BadCredentialsException;
import com.leets.backend.blog.exception.DuplicateEmailException;
import com.leets.backend.blog.exception.DuplicateNicknameException;
import com.leets.backend.blog.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
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
        // Spring Security의 AuthenticationManager를 사용하여 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 기존 로직 유지 (AuthenticationManager가 이미 인증을 처리했으므로 비밀번호 검증은 필요 없음)
        return userRepository.findByEmail(request.getEmail())
                .orElseThrow(BadCredentialsException::new); // 이메일로 찾지 못하면 예외 발생
    }
}
