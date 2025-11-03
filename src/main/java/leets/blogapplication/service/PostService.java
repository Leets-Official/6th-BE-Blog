package leets.blogapplication.service;

import leets.blogapplication.domain.Post;
import leets.blogapplication.dto.req.PostReq;
import leets.blogapplication.dto.res.PostRes;
import leets.blogapplication.repository.PostRepository;
import leets.blogapplication.repository.UserRepository;
import leets.blogapplication.service.auth.TokenService;
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
    private final TokenService tokenService;

    public PostService(PostRepository postRepository, UserRepository userRepository
    , TokenService tokenService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.tokenService = tokenService;
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
        Post post = Post.create(tokenService.getUserFromAccessToken()
                , req.getTitle(), req.getContent(), req.getCreatedAt(), req.getUpdatedAt());
        return postRepository.save(post).getId();
    }


    // 수정 (변경감지)
    @Transactional
    public void update(PostReq req) {
        User user = tokenService.getUserFromAccessToken(); //수정하려는 사람
        Post post = postRepository.findById(req.getId())
                .orElseThrow(() -> new RuntimeException("Post not found")); //수정하려는 post
        if(user.getId().equals(post.getUser().getId())) {
            postRepository.updatePost(req.getTitle(), req.getContent(), req.getUpdatedAt(), req.getId());
        } else {
            throw new RuntimeException("Author and modifier are different");
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