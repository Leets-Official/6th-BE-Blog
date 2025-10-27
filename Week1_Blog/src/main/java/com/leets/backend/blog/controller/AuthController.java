package com.leets.backend.blog.controller;

import com.leets.backend.blog.DTO.*;
import com.leets.backend.blog.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthService service;

    public AuthController(AuthService service) { this.service = service; }

    //@PostMapping("/login")
    //public LoginResponseDTO Login(LoginRequestDTO dto){
    //    return service.UserLogin(dto);
    //}
}
