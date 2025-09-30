package com.leets.backend.blog.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "post_image")
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image")
    private Long imageId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "image_order", nullable = false)
    private Integer imageOrder;

    // Getters
    public Long getImageId() {
        return imageId;
    }
    public Post getPost() {
        return post;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public Integer getImageOrder() {
        return imageOrder;
    }
}
