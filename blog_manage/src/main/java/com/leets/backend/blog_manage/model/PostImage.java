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

    @Column(name = "image_order")
    private Integer imageOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public PostImage() {}

    // --- Getters ---
    public Long getId() { return id; }
    public String getImageUrl() { return imageUrl; }
    public Integer getImageOrder() { return imageOrder; }
    public Post getPost() { return post; }
}