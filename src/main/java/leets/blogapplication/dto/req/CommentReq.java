package leets.blogapplication.dto.req;

public class CommentReq {
    String content;
    Long postId;
    Long userId;
    Long parentId;

    public String getContent() { return content; }
    public Long getPostId() { return postId; }
    public Long getUserId() { return userId; }
    public Long getParentId() { return parentId; }
}
