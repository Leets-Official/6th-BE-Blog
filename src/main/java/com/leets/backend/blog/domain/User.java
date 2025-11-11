package com.leets.backend.blog.domain; // 패키지 경로는 기존과 동일하게 유지

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false)
    private String password; // (OAuth2 사용자는 이 필드를 사용하지 않음)

    @Column(nullable = false, unique = true, length = 20)
    private String nickname;

    @Column(length = 30)
    private String introduction;

    private String profileImageUrl;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // --- 기본 생성자 (JPA용) ---
    public User() {}

    // ========== 1. [OAuth2 추가 부분] OAuth2 신규 가입용 생성자 ==========
    /**
     * OAuth2 (Kakao)를 통해 신규 가입하는 사용자를 위한 생성자입니다.
     * @param email 카카오에서 받은 이메일
     * @param nickname 카카오에서 받은 닉네임
     * @param profileImageUrl 카카오에서 받은 프로필 사진 URL
     */
    public User(String email, String nickname, String profileImageUrl) {
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        // OAuth2 사용자는 별도 비밀번호가 없으므로, @Column(nullable = false)를 만족시키기 위한
        // 임시값(혹은 UUID)을 넣어줍니다.
        this.password = "OAUTH2_USER_PASSWORD_PLACEHOLDER";
    }

    // --- 기존 Getter/Setter (변경 없음) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public String getIntroduction() { return introduction; }
    public void setIntroduction(String introduction) { this.introduction = introduction; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<Post> getPosts() { return posts; }
    public void setPosts(List<Post> posts) { this.posts = posts; }
    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }


    // --- UserDetails 구현 메서드 (기존과 동일) ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email; // Spring Security에서 username = email
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }


    // ========== 2. [OAuth2 추가 부분] 프로필 업데이트 메서드 ==========
    /**
     * 카카오에서 받아온 닉네임, 프로필 이미지 URL로 기존 정보를 업데이트합니다.
     */
    public User update(String nickname, String profileImageUrl) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        return this;
    }

    // ========== 3. [OAuth2 추가 부분] 권한(Role) 반환 메서드 ==========
    /**
     * CustomOAuth2UserService에서 사용자의 권한을 참조할 때 사용합니다.
     * getAuthorities()와 일치하도록 "ROLE_USER"를 반환합니다.
     */
    public String getRoleKey() {
        // 현재 UserDetails 구현에서 "ROLE_USER"로 고정되어 있으므로, 동일한 값을 반환
        return "ROLE_USER";
    }
}