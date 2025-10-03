package com.leets.backend.blog.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "postImgs") // DB 테이블명
public class PostImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가
    @Column(name = "img_id")
    private Long imgId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    // 썸네일 이미지는 sortOrder = 0 설정으로 변경
    //@Column(name = "is_thumbnail")
    //private Boolean isThumbnail;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    public PostImg(Long postId, String imgUrl, Integer sortOrder) {
        this.postId = postId;
        this.imgUrl = imgUrl;
        this.sortOrder = sortOrder;
    }

    public Long getImgId() {
        return imgId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Boolean getIsThumbnail() {
        return isThumbnail;
    }

    public void setIsThumbnail(Boolean thumbnail) {
        isThumbnail = thumbnail;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
}
