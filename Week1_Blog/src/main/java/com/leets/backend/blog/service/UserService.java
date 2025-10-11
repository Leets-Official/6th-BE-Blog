package com.leets.backend.blog.service;

import com.leets.backend.blog.DTO.UserResponseDTO;
import com.leets.backend.blog.DTO.UserCreateRequestDTO;
import com.leets.backend.blog.DTO.UserUpdateRequestDTO;
import com.leets.backend.blog.domain.Post;
import com.leets.backend.blog.domain.User;
import com.leets.backend.blog.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserResponseDTO GetUserByUserId(Long userId){
        User user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        return new UserResponseDTO(user);
    }

    public UserResponseDTO UpdateUserByUserId(Long userId, UserUpdateRequestDTO dto){
        User user = repository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));

        user.updateUser(
                user.getNickname() == dto.getNickname() ? null : dto.getNickname(),
                user.getEmail() == dto.getEmail() ? null : dto.getEmail(),
                dto.getPassword(),
                dto.getIntroduction(),
                dto.getName(),
                dto.getBirthDate().toString()
        );

        User updatedUser = repository.save(user);
        return new UserResponseDTO(updatedUser);
    }

    public UserResponseDTO CreateUser(UserCreateRequestDTO dto){
        User newUser = User.createUser(dto);

        User savedUser = repository.save(newUser);

        return new UserResponseDTO(savedUser);
    }
}
