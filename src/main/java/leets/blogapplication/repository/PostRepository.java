package leets.blogapplication.repository;

import leets.blogapplication.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {
    // C
    Post create(Post post);
    // R(전체)
    List<Post> findAll();
    //R (상세)
    Optional<Post> findById(String id);
    //U
    Post update(String id, Post post);
    //D
    void delete(String id);
    //검색
    List<Post> findByTitle(String title);

}