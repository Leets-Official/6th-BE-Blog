package com.leets.backend.blog.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_image")
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false, length = 500)
    private String imageUrl;

    @Column(name = "is_thumbnail", nullable = false)
    private boolean isThumbnail = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "order_index", nullable = false)
    private int orderIndex = 0; // 기본값 0

    protected PostImage() {}

    // Getters
    public Long getImageId() { return imageId; }
    public Post getPost() { return post; }
    public String getImageUrl() { return imageUrl; }
    public boolean isThumbnail() { return isThumbnail; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public int getOrderIndex() { return orderIndex; }
}
