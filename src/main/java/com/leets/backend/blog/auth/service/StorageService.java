package com.leets.backend.blog.auth.service;

import com.leets.backend.blog.common.exception.CustomException;
import com.leets.backend.blog.common.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class StorageService {

    // 로컬 파일 저장을 위한 임시 경로 (실제 운영 환경에서는 S3/GCS 사용)
    private final Path uploadDir = Paths.get("uploads");

    public String upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new CustomException(ErrorCode.BAD_REQUEST, "업로드할 파일이 비어있습니다.");
        }
        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 실제 서비스에서는 S3 URL 반환
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("프로필 이미지 업로드 실패", e);
        }
    }
}
