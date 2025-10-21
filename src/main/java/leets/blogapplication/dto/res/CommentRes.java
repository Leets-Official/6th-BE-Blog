package leets.blogapplication.dto.res;

import leets.blogapplication.domain.Comment;

public class CommentRes {
    Long id;
    String content;
    String nickname;
    Long parentId;

    public CommentRes(Long id, String content, String nickname) {
        this.id = id;
        this.content = content;
        this.nickname = nickname;
    }

    public CommentRes(Long id, String content, String nickname, Long parentId) {
        this.id = id;
        this.content = content;
        this.nickname = nickname;
        this.parentId = parentId;
    }

    public Long getId() { return id; }
    public String getContent() { return content; }
    public String getNickname() { return nickname; }
}
