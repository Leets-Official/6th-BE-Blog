package com.leets.backend.blog.controller;

import com.leets.backend.blog.DTO.UserCreateRequestDTO;
import com.leets.backend.blog.DTO.UserUpdateRequestDTO;
import com.leets.backend.blog.DTO.UserResponseDTO;
import com.leets.backend.blog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public UserResponseDTO GetUserInfo(@PathVariable("id") Long userId){
        return service.GetUserByUserId(userId);
    }

    @PutMapping("/{id}")
    public UserResponseDTO UpdateUser(
            @PathVariable("id") Long userId,
            @Valid  @RequestBody UserUpdateRequestDTO dto){

        return service.UpdateUserByUserId(userId, dto);
    }

    @PostMapping("/create")
    public UserResponseDTO CreateUser(@Valid @RequestBody UserCreateRequestDTO dto){
        return service.CreateUser(dto);
    }
}
