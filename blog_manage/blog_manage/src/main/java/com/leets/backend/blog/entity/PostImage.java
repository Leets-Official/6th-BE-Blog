package com.leets.backend.blog.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "post_image")
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMAGE_ID") // BIGINT 매핑
    private Long imageId;

    @ManyToOne(fetch = FetchType.LAZY) // N:1 관계
    @JoinColumn(name = "POST_ID", nullable = false) // FK 컬럼
    private Post post;

    @Column(name = "IMAGE_URL", length = 255, nullable = false)
    private String imageUrl;

    @Column(name = "DISPLAY_ORDER", nullable = false)
    private int displayOrder; // 이미지 표시 순서 (INT 매핑)


    public PostImage() {}

    // Getter (Lombok 금지)
    public Long getImageId() { return imageId; }
    public Post getPost() { return post; }
    public String getImageUrl() { return imageUrl; }
    public int getDisplayOrder() { return displayOrder; }
}