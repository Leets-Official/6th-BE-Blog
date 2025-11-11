package com.leets.backend.blog.controller;

import com.leets.backend.blog.dto.request.UserUpdateRequestDTO;
import com.leets.backend.blog.dto.response.UserResponseDTO;
import com.leets.backend.blog.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "회원 정보 관리")
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Operation(
            summary = "회원 정보 상세 조회",
            description = "회원 이름, 이메일, 비밀번호 등의 상세 정보를 조회"
    )
    @GetMapping("/{id}")
    public UserResponseDTO getUserInfo(@PathVariable("id") Long userId){
        return service.getUserByUserId(userId);
    }

    @Operation(
            summary = "회원 정보 수정",
            description = "기존 회원에 대한 정보를 수정"
    )
    @PutMapping("/{id}")
    public UserResponseDTO updateUser(
            @PathVariable("id") Long userId,
            @Valid  @RequestBody UserUpdateRequestDTO dto){

        return service.updateUserByUserId(userId, dto);
    }
}
