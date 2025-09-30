package com.leets.backend.blog.model;

import jakarta.persistence.*;

@Entity @Table(name="post_image")
public class PostImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="image_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="content_id", nullable = false)
    private Post post;

    @Column(name="image_url", length = 255, nullable = false)
    private String imageUrl;

    @Column(name="oreder_no")
    private Integer order;

    public PostImage(){}

    public Long getId(){return id;}
    public Post getPost(){return post;}
    public String getImageUrl(){return imageUrl;}
    public Integer getOrder(){return order;}
}
