package com.leets.backend.blog.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name="user") //이 클래스를 JPA 엔티티로 등록하고 DB의 user 테이블에 매핑한다
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @Column(name="auth_identities", length=16, nullable=false)
    private String authIdentities;

    @Column(nullable=false, length=90)
    private String email;

    @Column(nullable=false, length=255)
    private String pas;

    @Column(nullable=false, length=50)
    private String name;

    private LocalDate birth;

    @Column(length=255)
    private String info;

    @Column(length=40)
    private String nick;

    @Column(name="profile_image", length =255)
    private String profileImage;

    @Column(name="kakao_id", length=45)
    private String kakaoId;

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "author")
    private List<Post>posts=new ArrayList<>();

    @OneToMany(mappedBy="author")
    private List<Comment>comments=new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Token> tokens=new ArrayList<>();

    public Long getId(){return id;}
    public String getAuthIdentities(){return authIdentities;}
    public String getEmail(){return email;}
    public String getPas() {return pas;}
    public String getName(){return name;}
    public LocalDate getBirth(){return birth;}


}
