package com.leets.backend.blog.service;

// 1. 필요한 Import
import com.leets.backend.blog.domain.User;
import com.leets.backend.blog.repository.UserRepository;
import com.leets.backend.blog.config.oauth.OAuthAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    // 2. Logger 수동 초기화 (@Slf4j 대신 사용)
    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2UserService.class);

    // 3. final 필드
    private final UserRepository userRepository;

    // 4. @RequiredArgsConstructor 대신 수동 생성자 주입
    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 1. 기본 OAuth2UserService 객체를 사용하여 사용자 정보 가져오기
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 2. 현재 로그인 진행 중인 서비스(kakao)를 구분
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // 3. OAuth2 로그인 시 키가 되는 필드 값 (카카오는 "id"를 사용)
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint()
                .getUserNameAttributeName();

        // 4. OAuthAttributes: 가져온 사용자 정보(Map)를 분석하여 DTO로 변환
        Map<String, Object> attributes = oAuth2User.getAttributes();
        log.info("[OAuth2User Attributes] : {}", attributes);

        // 5. 카카오 응답 파싱 (OAuthAttributes 클래스가 담당)
        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, attributes);

        // 6. 사용자 저장 또는 업데이트 (DB 처리)
        User user = saveOrUpdate(oAuthAttributes);

        // 7. Spring Security가 인증을 위해 사용하는 객체 반환
        return new DefaultOAuth2User(
                // 권한 부여 (예: ROLE_USER)
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                oAuthAttributes.getAttributes(),
                oAuthAttributes.getNameAttributeKey());
    }

    /**
     * DB에 사용자 정보를 저장하거나, 이미 있다면 정보를 업데이트합니다.
     */
    private User saveOrUpdate(OAuthAttributes attributes) {

        // 이메일로 기존 사용자 찾기
        User user = userRepository.findByEmail(attributes.getEmail())
                // 2. 기존 사용자가 있다면, 이름과 프로필 사진만 업데이트
                .map(entity -> {
                    log.info("Existing user found. Updating info... (Email: {})", attributes.getEmail());
                    return entity.update(attributes.getName(), attributes.getPicture());
                })
                // 3. 기존 사용자가 없다면, 새로운 User 엔티티 생성 (회원가입)
                .orElseGet(() -> {
                    log.info("New user. Registering... (Email: {})", attributes.getEmail());
                    return attributes.toEntity();
                });

        return userRepository.save(user); // DB에 저장
    }
}