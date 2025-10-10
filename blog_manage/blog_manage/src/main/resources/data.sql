INSERT INTO `user` (`USER_ID`, `NAME`, `EMAIL`, `PASSWORD`, `NICKNAME`, `BIRTH_DATE`, `INTRO`, `PROFILE_URL`, `LOGIN_TYPE`, `KAKAO_ID`, `CREATED_AT`, `UPDATED_AT`)
VALUES (1, '임시사용자', 'test@example.com', 'test1234', '임시닉네임', '2000-01-01', '자기소개입니다.', null, 'EMAIL', null, NOW(), NOW());

INSERT INTO `post` (`USER_ID`, `TITLE`, `CONTENT`, `CREATED_AT`, `UPDATED_AT`) VALUES (1, '첫 번째 게시글', '첫 번째 게시글 내용입니다.', NOW(), NOW());
INSERT INTO `post` (`USER_ID`, `TITLE`, `CONTENT`, `CREATED_AT`, `UPDATED_AT`) VALUES (1, '두 번째 게시글', '두 번째 게시글 내용입니다. 테스트 중입니다.', NOW(), NOW());
INSERT INTO `post` (`USER_ID`, `TITLE`, `CONTENT`, `CREATED_AT`, `UPDATED_AT`) VALUES (1, '세 번째 게시글', '세 번째 게시글 내용입니다. API가 잘 동작하네요.', NOW(), NOW());
INSERT INTO `post` (`USER_ID`, `TITLE`, `CONTENT`, `CREATED_AT`, `UPDATED_AT`) VALUES (1, '네 번째 게시글', '네 번째 게시글 내용입니다. 더미 데이터 생성.', NOW(), NOW());
INSERT INTO `post` (`USER_ID`, `TITLE`, `CONTENT`, `CREATED_AT`, `UPDATED_AT`) VALUES (1, '다섯 번째 게시글', '다섯 번째 게시글 내용입니다. 리스트 조회 테스트.', NOW(), NOW());
