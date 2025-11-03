package com.leets.backend.blog.service;

import com.leets.backend.blog.dto.response.UserResponseDTO;
import com.leets.backend.blog.dto.request.UserCreateRequestDTO;
import com.leets.backend.blog.dto.request.UserUpdateRequestDTO;
import com.leets.backend.blog.entity.User;
import com.leets.backend.blog.exception.CustomException;
import com.leets.backend.blog.exception.ErrorCode;
import com.leets.backend.blog.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserResponseDTO getUserByUserId(Long userId){
        User user = repository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return new UserResponseDTO(user);
    }

    public UserResponseDTO updateUserByUserId(Long userId, UserUpdateRequestDTO dto){
        User user = repository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updateUser(
                user.getNickname().equals(dto.getNickname()) ? null : dto.getNickname(),
                user.getEmail().equals(dto.getEmail()) ? null : dto.getEmail(),
                dto.getPassword(),
                dto.getIntroduction(),
                dto.getName(),
                dto.getBirthDate().toString(),
                dto.getProfileImgUrl()
        );

        User updatedUser = repository.save(user);
        return new UserResponseDTO(updatedUser);
    }

    public UserResponseDTO createUser(UserCreateRequestDTO dto){
        User newUser = User.createUser(dto);

        User savedUser = repository.save(newUser);

        return new UserResponseDTO(savedUser);
    }
}
