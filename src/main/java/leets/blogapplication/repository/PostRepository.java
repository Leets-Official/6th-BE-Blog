package leets.blogapplication.repository;

import leets.blogapplication.controller.PostController;
import leets.blogapplication.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByTitle(String title);
    //얘네들 내가 의도한 대로 작동하는지 확인 한 번만 하기
}