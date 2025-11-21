package com.leets.backend.blog.service;

import com.leets.backend.blog.config.CustomUserDetails;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.oauth.KakaoUserInfo;
import com.leets.backend.blog.oauth.OAuth2UserInfo;
import com.leets.backend.blog.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());

        String socialId = oAuth2UserInfo.getSocialId();
        String provider = oAuth2UserInfo.getProvider();

        User user = userRepository.findByKakaoId(socialId).orElseGet(() -> {
            String nickname = oAuth2UserInfo.getNickname();
            // If nickname is null or duplicate, generate a random one
            if (nickname == null || userRepository.existsByNickname(nickname)) {
                nickname = "user_" + UUID.randomUUID().toString().substring(0, 8);
            }

            User newUser = new User(
                    oAuth2UserInfo.getEmail(),
                    null, // Social login users don't have a password
                    nickname,
                    provider.toUpperCase()
            );
            newUser.setKakaoId(socialId);
            newUser.setProfileUrl(oAuth2UserInfo.getProfileImageUrl());
            return userRepository.save(newUser);
        });

        // Add this part to update user info on every login
        user.setProfileUrl(oAuth2UserInfo.getProfileImageUrl());
        // The nickname from Kakao might change, so we can update it as well.
        // But for now, we will only update the profile image.
        userRepository.save(user);

        return new CustomUserDetails(user, oAuth2User.getAttributes());
    }

    private OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase("kakao")) {
            return new KakaoUserInfo(attributes);
        }
        // Add other providers like Google, Naver here
        // else if (registrationId.equalsIgnoreCase("google")) {
        //     return new GoogleUserInfo(attributes);
        // }
        throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
    }
}