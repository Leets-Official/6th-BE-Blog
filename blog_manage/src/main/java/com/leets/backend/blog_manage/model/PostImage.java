package com.leets.backend.blog_manage.model;

import jakarta.persistence.*;

@Entity
@Table(name = "post_image")
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @Column(name = "\"order\"") // 'order'는 SQL 예약어이므로 큰따옴표로 감싸줌
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public PostImage() {}

    // --- Getters ---
    public Long getId() { return id; }
    public String getImageUrl() { return imageUrl; }
    public Integer getOrder() { return order; }
    public Post getPost() { return post; }
}