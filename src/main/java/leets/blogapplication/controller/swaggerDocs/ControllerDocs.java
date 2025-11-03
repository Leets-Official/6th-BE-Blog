package leets.blogapplication.controller.swaggerDocs;

import leets.blogapplication.dto.req.CommentReq;
import leets.blogapplication.dto.req.PostReq;
import leets.blogapplication.dto.res.CommentRes;
import leets.blogapplication.dto.res.PostRes;
import leets.blogapplication.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Test", description = "Controller API입니다.")
public interface ControllerDocs {

    @Operation(summary = "post에 대해 add comment", description = "post에 대해 add comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "comment 저장 완료"),
            @ApiResponse(responseCode = "500", description = "comment 저장 실패") })
    public ResponseEntity<Void> addComment(@RequestBody CommentReq req);

    @Operation(summary = "post에 대한 comment 전체 get", description = "post에 대한 최상위 comment get")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "comment return 성공"),
            @ApiResponse(responseCode = "500", description = "comment return 실패") })
    public ResponseEntity<List<CommentRes>> getComment(@PathVariable Long postId);

    @Operation(summary = "하위 comment get", description = "특정 최상위 comment에 대한 하위 comment get")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "comment return 성공"),
            @ApiResponse(responseCode = "500", description = "comment return 실패") })
    public ResponseEntity<List<CommentRes>> getChildComment(@PathVariable Long commentId);

    @Operation(summary = "getAllPosts", description = "post 보는 메인 페이지, 페이지네이션 있음")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "post return 성공"),
            @ApiResponse(responseCode = "500", description = "post return 실패") })
    public ResponseEntity<List<PostRes>> getPosts(@RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                                  @RequestParam(required = false, defaultValue = "createdAt", value = "criteria") String criteria);

    @Operation(summary = "findPostById", description = "post의 Id 기준으로 찾기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "post return 성공"),
            @ApiResponse(responseCode = "500", description = "post return 실패") })
    public ResponseEntity<PostRes> getPostById(@PathVariable Long id);

    @Operation(summary = "findPostByTitle", description = "제목 기준으로 post find, 페이지네이션 있음")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "post return 성공"),
            @ApiResponse(responseCode = "500", description = "post return 실패") })
    public ResponseEntity<List<PostRes>> getPostByTitle(@PathVariable String title,
                                                        @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                                        @RequestParam(required = false, defaultValue = "createdAt", value = "criteria") String criteria
    );


    @Operation(summary = "post 생성", description = "post 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "post create 성공"),
            @ApiResponse(responseCode = "500", description = "post create 실패") })
    public ResponseEntity<Void> createPost(@RequestBody PostReq req);

    @Operation(summary = "post 업데이트", description = "post 업데이트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "post update 성공"),
            @ApiResponse(responseCode = "500", description = "post update 실패") })
    public ResponseEntity<Void> updatePost(@RequestBody PostReq req);

    @Operation(summary = "post 삭제", description = "post 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "post delete 성공"),
            @ApiResponse(responseCode = "500", description = "post delete 실패") })
    public ResponseEntity<Void> deletePost(@PathVariable Long id);
}
