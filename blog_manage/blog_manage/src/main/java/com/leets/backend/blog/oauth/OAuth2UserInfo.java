package com.leets.backend.blog.oauth;

import java.util.Map;

public interface OAuth2UserInfo {
    String getProvider();
    String getSocialId();
    String getEmail();
    String getNickname();
    String getProfileImageUrl();
}
