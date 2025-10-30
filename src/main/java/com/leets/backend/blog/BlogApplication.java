package com.leets.backend.blog;

import com.leets.backend.blog.domain.User;
import com.leets.backend.blog.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BlogApplication {

    private final UserRepository userRepository;

    public BlogApplication(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

    /**
     * 애플리케이션 시작 시 테스트용 데이터를 추가하는 메서드
     */
    @PostConstruct
    public void initTestData() {
        if (userRepository.count() == 0) {
            System.out.println("======= 테스트 데이터 초기화 =======");
            User testUser = new User();
            testUser.setEmail("test@test.com");
            testUser.setPassword("password123");
            testUser.setNickname("테스트유저");

            userRepository.save(testUser); // DB에 저장
            System.out.println("ID 1번 유저 생성 완료");
            System.out.println("================================");
        }
    }
}