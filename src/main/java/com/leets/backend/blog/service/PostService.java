package com.leets.backend.blog.service;

import com.leets.backend.blog.controller.dto.PostRequest;
import com.leets.backend.blog.controller.dto.PostResponse;
import com.leets.backend.blog.model.Post;
import com.leets.backend.blog.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository repository;

    //의존성 주입(Service가 Repository라는 도구 사용할 수 있게 연결)
    public PostService(PostRepository repository){
        this.repository = repository;
    }

    //저장된 모든 글을 Repository에서 꺼내와서 Controller에게 반환하는 메서드
    public List<Post> findAll(){
        return repository.findAll();
    }

    //DB 저장 과정
    public PostResponse save(PostRequest request){
        Post post = new Post(request.getTitle(), request.getContent());
        Post saved = repository.save(post);
        return new PostResponse(saved.getId(), saved.getTitle(), saved.getContent());
    }
}
