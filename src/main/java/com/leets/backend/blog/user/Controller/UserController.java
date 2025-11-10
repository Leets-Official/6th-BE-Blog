package com.leets.backend.blog.user.controller;

import com.leets.backend.blog.common.response.ApiResponse;
import com.leets.backend.blog.user.controller.dto.request.UserPasswordUpdateRequest;
import com.leets.backend.blog.user.controller.dto.request.UserRequest;
import com.leets.backend.blog.user.controller.dto.response.UserResponse;
import com.leets.backend.blog.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "회원 정보 관리")
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 사용자 조회
    @Operation(
            summary = "회원 정보 상세 조회",
            description = "상세 정보 조회(회원 이름, 이메일, 비밀번호)"
    )
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long id) {
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 닉네임 변경
    @PatchMapping("/{userId}/nickname")
    public ResponseEntity<ApiResponse<UserResponse>> updateNickname(
            @PathVariable Long id,
            @Valid @RequestBody UserRequest request) {

        UserResponse response = userService.updateNickname(id, request.getNickname());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 비밀번호 변경
    @PatchMapping("/{userId}/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(
            @PathVariable Long id,
            @Valid @RequestBody UserPasswordUpdateRequest request) {

        userService.updatePassword(id, request.getOldPassword(), request.getNewPassword());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    // 프로필 사진 변경 추후 구현
}
