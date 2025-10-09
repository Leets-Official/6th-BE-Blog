package leets.blogapplication.repository;

import leets.blogapplication.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
//    // C
//    PostM create(PostM post);
//    // R(전체)
//    List<PostM> findAll();
//    //R (상세)
//    Optional<PostM> findById(String id);
//    //U
//    PostM update(String id, PostM post);
//    //D
//    void delete(String id);
//    //검색
//    List<PostM> findByTitle(String title);

}