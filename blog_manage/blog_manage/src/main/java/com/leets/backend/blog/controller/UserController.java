package com.leets.backend.blog.controller;

import com.leets.backend.blog.common.dto.ApiResponse;
import com.leets.backend.blog.dto.UserResponse;
import com.leets.backend.blog.dto.UserUpdateRequest;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserResponse userResponse = new UserResponse(user);
        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request) {
        // TODO: 현재 로그인한 사용자와 id가 일치하는지 확인하는 로직 필요
        User updatedUser = userService.updateUser(id, request);
        UserResponse userResponse = new UserResponse(updatedUser);
        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }
}
