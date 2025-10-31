package com.leets.backend.blog.exception;

public class CommentNotFoundException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "댓글을 찾을 수 없습니다.";

    public CommentNotFoundException(){
        super(DEFAULT_MESSAGE);
    }

    public CommentNotFoundException(Long commentId) {
        super(DEFAULT_MESSAGE + "(ID : " + commentId + ")");
    }

    public CommentNotFoundException(Long commentId, Long postId) {
        super("해당 게시물(ID: " + postId + ")에서 댓글(ID: " + commentId + ")을 찾을 수 없습니다.");
    }
}
