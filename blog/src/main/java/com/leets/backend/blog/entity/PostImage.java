package com.leets.backend.blog.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "post_image")
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;
    @Column(nullable = false)
    private String imageUrl;
    @Column(nullable = false)
    private boolean isThumbnail;
    private Integer orderIndex;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id", nullable = false)
    private Post post;

    //Getters
    public Long getImageId() {
        return imageId;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public boolean isThumbnail() {
        return isThumbnail;
    }
    public Integer getOrderIndex() {
        return orderIndex;
    }
    public Post getPost() {
        return post;
    }
}
