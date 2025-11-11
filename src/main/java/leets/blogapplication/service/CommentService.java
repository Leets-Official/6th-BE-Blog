package leets.blogapplication.service;

import leets.blogapplication.domain.Comment;
import leets.blogapplication.domain.Post;
import leets.blogapplication.domain.User;
import leets.blogapplication.dto.req.CommentReq;
import leets.blogapplication.dto.res.CommentRes;
import leets.blogapplication.repository.CommentRepository;
import leets.blogapplication.repository.PostRepository;
import leets.blogapplication.repository.UserRepository;
import leets.blogapplication.service.auth.TokenService;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    protected CommentService(CommentRepository commentRepository, PostRepository postRepository,
                             UserRepository userRepository, TokenService tokenService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public void saveComment(CommentReq req){
        Post p = postRepository.findById(req.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User u = tokenService.getUserFromAccessToken();
        Comment parent = commentRepository.findById(req.getParentId())
                        .orElse(null);
        Comment child = Comment.create(req.getContent(), p, u, parent);
        commentRepository.save(child);
    }

    public List<CommentRes> getComment(Long postId){
        return commentRepository.findCommentsWithUserNickname(postId);
    }

    public List<CommentRes> getChildComment(Long commentId){
        return commentRepository.findChildCommentsWithUserNickname(commentId);
    }
}
