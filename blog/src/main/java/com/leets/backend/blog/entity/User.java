package com.leets.backend.blog.entity;

import com.leets.backend.blog.enums.LoginMethod;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    @Column(nullable = false, length = 10)
    private String name;
    private LocalDate birthdate;
    @Column(nullable = false, unique = true, length = 20)
    private String nickname;
    @Column(length = 30)
    private String introduction;
    private String profileImage;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LoginMethod loginMethod;

    @Column(nullable = false)
    private String roles = "ROLE_USER";

    @Column(unique = true)
    private String kakaoId;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public User() {}

    public User(String email, String password, String name, String nickname, String profileImage, LoginMethod loginMethod, String introduction, LocalDate birthdate, String roles) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.loginMethod = loginMethod;
        this.introduction = introduction;
        this.birthdate = birthdate;
        this.roles = (roles == null || roles.isEmpty()) ? "ROLE_USER" : roles;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    //Getters
    public Long getUserId() {
        return userId;
    }
    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }
    public LocalDate getBirthdate() {
        return birthdate;
    }
    public String getNickname() {
        return nickname;
    }
    public String getIntroduction() {
        return introduction;
    }
    public String getProfileImage() {
        return profileImage;
    }
    public LoginMethod getLoginMethod() {
        return loginMethod;
    }
    public String getKakaoId() {
        return kakaoId;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(this.roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠금 여부
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명(비밀번호) 만료 여부
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}