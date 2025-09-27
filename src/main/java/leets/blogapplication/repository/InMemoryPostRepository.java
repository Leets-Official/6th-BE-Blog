package leets.blogapplication.repository;

import leets.blogapplication.model.Post;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryPostRepository implements PostRepository {
    private final Map<String, Post> store = new ConcurrentHashMap<>();

    @Override
    public Post create(Post post) {
        if(post.getTitle() == null || post.getTitle().isEmpty()){
            throw new IllegalArgumentException("Title cannot be null or empty");
        } else if(post.getContent() == null || post.getContent().isEmpty()){
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
        LocalDateTime now = LocalDateTime.now();
        post.setCreatedAt(now);
        String id = UUID.randomUUID().toString();
        post.setId(id);
        store.put(id, post);
        //setId의 id와 put의 id가 같은 id가 되긴 해?
        return post;
    }

    @Override
    public List<Post> findAll(){
        List<Post> posts = new ArrayList<>(store.values());
        posts.sort(Comparator.comparing(Post::getId));
        //일단 이렇게 쓰긴 하는데 created_at 아니면 updated_at으로 정렬성 보장할 수 있는 방법 강구하기
        return posts;
    }

    @Override
    public Optional<Post> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Post update(String id, Post post) {
        if(post.getTitle() == null || post.getTitle().isEmpty() || !store.containsKey(post.getId())){
            throw new IllegalArgumentException("Post does not exist");
        }
        LocalDateTime now = LocalDateTime.now();
        store.get(id).setTitle(post.getTitle()).setContent(post.getContent()).setUpdatedAt(now);
        return post;
    }

    @Override
    public List<Post> findByTitle(String title) {
        if (title == null || title.isBlank()) return findAll();
        String q = title.toLowerCase();

        List<Post> result = new ArrayList<>();
        for (Post p : store.values()) {
            String t = p.getTitle();
            if (t != null && t.toLowerCase().contains(q)) {
                result.add(p);
            }
        }

        // 결과 정렬 규칙(원하는 대로 바꿔도 됨): createdAt DESC
        result.sort(Comparator.comparing(Post::getCreatedAt,
                Comparator.nullsLast(Comparator.naturalOrder())).reversed());
        return result;
    }

    @Override
    public void delete(String id) {
        if(!store.containsKey(id)){
            throw new IllegalArgumentException("Post does not exist");
        }
        store.remove(id); //id만 없애도 찾을 방법은 없음이 맞나? 내용까지 다 삭제 안해도 되려나?
    }
}
