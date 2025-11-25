package com.leets.backend.blog.config.oauth;
import com.leets.backend.blog.domain.User;

import java.util.Map;

// 5. @Getter, @Builder를 사용하지 않는 수동 DTO
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        // 지금은 "kakao"만 처리하지만, 나중에 "google", "naver" 등을 추가할 수 있습니다.
        if ("kakao".equals(registrationId)) {
            return ofKakao(userNameAttributeName, attributes);
        }

        // throw new OAuth2AuthenticationException("Unsupported registrationId: " + registrationId);
        // 또는 기본값 반환
        return ofKakao(userNameAttributeName, attributes); // 임시로 카카오만 처리
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        // 1. "kakao_account" 맵 추출
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        // 2. "kakao_account" 안의 "profile" 맵 추출
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        String email = (String) kakaoAccount.get("email");
        String name = (String) kakaoProfile.get("nickname");
        String picture = (String) kakaoProfile.get("profile_image_url"); // 프로필 이미지 URL

        return new OAuthAttributes(attributes,
                userNameAttributeName, // "id"
                name,
                email,
                picture);
    }

    /**
     * 이 DTO의 정보를 바탕으로 새로운 User 엔티티를 생성합니다.
     * (회원가입 시 사용)
     */
    public User toEntity() {
        return new User(email, name, picture);
    }

    // 8. CustomOAuth2UserService에서 사용할 Getter 메서드들
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public String getNameAttributeKey() {
        return nameAttributeKey;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPicture() {
        return picture;
    }
}