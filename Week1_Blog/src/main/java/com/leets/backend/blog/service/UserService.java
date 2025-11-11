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

        return UserResponseDTO.toDTO(user);
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
        return UserResponseDTO.toDTO(updatedUser);
    }
}
