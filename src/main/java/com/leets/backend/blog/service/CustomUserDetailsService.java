package com.leets.backend.blog.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leets.backend.blog.domain.User;
import com.leets.backend.blog.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // UserRepository를 주입받음
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Spring Security가 로그인 인증 시 호출하는 메서드
     * @param email (로그인 시 사용자가 입력한 이메일)
     * @return UserDetails (우리의 User 객체)
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // UserRepository를 통해 이메일로 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일을 찾을 수 없습니다: " + email));

        // User 객체는 UserDetails를 구현하고 있으므로 그대로 반환
        // (Spring Security가 이 UserDetails 객체를 받아 비밀번호를 비교함)
        return user;
    }
}
