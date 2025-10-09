package leets.blogapplication.controller;

import leets.blogapplication.service.PostService;
import org.springframework.stereotype.Controller;

@Controller
public class PostController {

    private final PostService postService;
    public PostController(PostService postService) { this.postService = postService; }

//    // 목록
//    @GetMapping("/posts")
//    public String list(Model model) {
//        model.addAttribute("posts", postService.getAllPosts());
//        return "posts";
//    }
//
//    // 작성 폼
//    @GetMapping("/post/new")
//    public String newForm(Model model) {
//        model.addAttribute("post", new PostM());
//        return "new-post";
//    }
//
//    // 작성 처리
//    @PostMapping("/post/new")
//    public String create(@ModelAttribute PostM post) {
//        PostM saved = postService.createPost(post);
//        // 목록으로 이동 (원하면 상세로 변경 가능)
//        return "redirect:/posts";
//    }
//
//    //상세조회
//    @GetMapping("/post/{id}")
//    public String detail(@PathVariable String id, Model model){
//        model.addAttribute("post", postService.getPostById(id));
//        return "post-detail";
//    }
//
//    // 상세 폼
//    @GetMapping("/posts/{title}")
//    public String findByTitle(@PathVariable String title, Model model) {
//        model.addAttribute("posts", postService.getPostByTitle(title)); // 템플릿 만들면 사용
//        model.addAttribute("q", title);
//        return "post-detail-title";
//    }
//
//    // 수정 폼
//    @GetMapping("/posts/{title}/edit")
//    public String editForm(@PathVariable String title, Model model) {
//        model.addAttribute("post", postService.getPostByTitle(title));
//        return "edit-post"; // 템플릿 만들면 사용
//    }
//
//    // 수정 처리
//    @PostMapping("/posts/{id}/edit")
//    public String edit(@PathVariable String id, @ModelAttribute PostM post) {
//        postService.updatePost(id, post);
//        return "redirect:/posts/{id}";
//    }
//
//    // 삭제 처리
//    @PostMapping("/posts/{id}/delete")
//    public String delete(@PathVariable String id) {
//        postService.deletePost(id);
//        return "redirect:/posts";
//    }
//    //http 메서드 적재적소에 다시 배치하기
}