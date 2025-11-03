package leets.blogapplication.service;

import leets.blogapplication.domain.Post;
import leets.blogapplication.dto.req.PostReq;
import leets.blogapplication.dto.res.PostRes;
import leets.blogapplication.repository.PostRepository;
import leets.blogapplication.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import leets.blogapplication.domain.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final int PAGE_SIZE = 10;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 전체 조회
    @Transactional
    public List<PostRes> getAll(int pageNo, String criteria) {
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE, Sort.by(Sort.Direction.DESC, criteria));
        return postRepository.findAll(pageable).getContent().stream().map(PostRes::transform).toList();
    }

    // 생성
    @Transactional
    public Long create(PostReq req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getPrincipal().toString();
        Long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
        Post post = Post.create(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not fount"))
                , req.getTitle(), req.getContent(), req.getCreatedAt(), req.getUpdatedAt());
        return postRepository.save(post).getId();
    }


    // 수정 (변경감지)
    @Transactional
    public void update(PostReq req) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getPrincipal().toString();

        User user = postRepository.findById(req.getId())
                .orElseThrow(() -> new RuntimeException("Post not found"))
                .getUser();
        if(user.getId().equals(userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")).getId())) {
            postRepository.updatePost(req.getTitle(), req.getContent(), req.getUpdatedAt(), req.getId());
        } else {
            throw new RuntimeException("Something wrong while updating post");
        }
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
    public PostRes getById(Long id) {
        return PostRes.transform(postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found: " + id)));
    }

    // 제목 검색
    public List<PostRes> getByTitle(String title, int pageNo, String criteria) {
        Pageable pageable = PageRequest.of(pageNo, PAGE_SIZE, Sort.by(Sort.Direction.DESC, criteria));
        return postRepository.findByTitle(title, pageable).getContent().stream().map(PostRes::transform).toList();
    }
}