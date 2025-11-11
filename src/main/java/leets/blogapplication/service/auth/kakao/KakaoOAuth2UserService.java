package leets.blogapplication.service.auth.kakao;

import leets.blogapplication.domain.User;
import leets.blogapplication.domain.UserSocial;
import leets.blogapplication.repository.UserRepository;
import leets.blogapplication.repository.UserSocialRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class KakaoOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserSocialRepository socialRepo;

    public KakaoOAuth2UserService(UserSocialRepository socialRepo) {
        this.socialRepo = socialRepo;
    }

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
        OAuth2User o = delegate.loadUser(req); // 카카오 userinfo 호출

        Map<String, Object> attr = o.getAttributes();
        Long kakaoId = (attr.get("id") instanceof Number n)
                ? n.longValue()
                : Long.parseLong(String.valueOf(attr.get("id")).trim()); // providerUserId
        String nickname;
        Object acc = attr.get("kakao_account");
        if (acc instanceof Map) {
            Object profile = ((Map<?, ?>) acc).get("profile");
            if (profile instanceof Map) nickname = (String) ((Map<?, ?>) profile).get("nickname");
            else {
                nickname = null;
            }
        } else {
            nickname = null;
        }

        UserSocial social = socialRepo
                .findByProviderAndProviderUserId("KAKAO", kakaoId)
                .orElseGet(() -> socialRepo.save(
                        buildNewSocial(kakaoId, nickname)));

        // 소셜 id를 principal로 사용
        Collection<GrantedAuthority> auth = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return new DefaultOAuth2User(auth, attr, "id") {
            public Long getSocialId() { return social.getId(); }
        };
    }

    private UserSocial buildNewSocial(Long subject, String nickname) {
        UserSocial s = new UserSocial();
        s.setProviderUserId(subject);
        s.setNickname(nickname);
        return s;
    }
}
