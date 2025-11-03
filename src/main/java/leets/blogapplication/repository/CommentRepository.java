package leets.blogapplication.repository;

import leets.blogapplication.domain.Comment;
import leets.blogapplication.dto.res.CommentRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT new leets.blogapplication.dto.res.CommentRes(c.id, c.content, u.nickname) " +
            "FROM Comment c JOIN c.user u " +
            "WHERE c.post.id = :postId AND c.parent IS NULL " +
            "ORDER BY c.id ASC")
    List<CommentRes> findCommentsWithUserNickname(@Param("postId") Long postId);

    @Query("SELECT new leets.blogapplication.dto.res.CommentRes(c.id, c.content, u.nickname, c.parent.id)" +
            "FROM Comment c JOIN c.user u " +
            "WHERE c.parent.id = :commentId " +
            "ORDER BY c.id ASC")
    List<CommentRes> findChildCommentsWithUserNickname(@Param("commentId") Long commentId);
}
