package leets.blogapplication.service;

import leets.blogapplication.controller.PostController;
import leets.blogapplication.domain.Post;
import leets.blogapplication.domain.User;
import leets.blogapplication.repository.PostRepository;
import leets.blogapplication.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 전체 조회
    public List<Post> getAll() {
        return postRepository.findAll();
    }

    // 생성
    @Transactional
    public Long create(Long id, String title, String content) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다.");
        }
        if (title.length() > 100) {
            throw new IllegalArgumentException("제목은 100자 이하만 허용됩니다.");
        }

        User user = userRepository.findById(id).orElse(null);

        leets.blogapplication.domain.Post p = new leets.blogapplication.domain.Post();
        p.setUser(user);
        p.setTitle(title);
        p.setContent(content);
        p.setCreatedAt(LocalDateTime.now());
        p.setUpdatedAt(LocalDateTime.now());

        return postRepository.save(p).getId();
    }


    // 수정 (변경감지)
    @Transactional
    public void update(Long id, String title, String content) {
        Post p = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));

        if (title != null) p.setTitle(title);
        if (content != null) p.setContent(content);
        p.setUpdatedAt(LocalDateTime.now());
        // 트랜잭션 종료 시 flush → update
    }

    // 삭제
    @Transactional
    public void delete(Long id) {
        if (!postRepository.existsById(id)) {
            throw new IllegalArgumentException("Post not found: " + id);
        }
        postRepository.deleteById(id);
    }

    // 단건 조회
    public Post getById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id));
    }

    // 제목 검색
    public List<Post> getByTitle(String title) {
        return postRepository.findByTitle(title);
        // 부분검색 쓰려면 repository 메서드 바꾸고 여기서도 연동
    }
}