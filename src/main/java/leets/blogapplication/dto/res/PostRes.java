package leets.blogapplication.dto.res;

import leets.blogapplication.domain.Post;
import java.time.LocalDateTime;

public class PostRes {
    Long id;
    String title;
    String content;
    Long author;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public static PostRes transform(Post post) {
        PostRes res = new PostRes();
        res.id = post.getId();
        res.title = post.getTitle();
        res.content = post.getContent();
        res.author = post.getUser().getId();
        res.createdAt = post.getCreatedAt();
        res.updatedAt = post.getUpdatedAt();
        return res;
    }
}
