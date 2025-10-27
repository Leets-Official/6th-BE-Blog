# 6th-BE-Blog

6기 BE 블로그 프로젝트 레포지토리입니다.

---
##  ⚠️ 미션 요구사항

1. 미션 진행 방법을 꼭 읽고 진행해주세요!!
   - 과거 자료라서 레포지토리 이름이 다릅니다! 6th-BE-Blog로 이해하시면 됩니다
   - [미션 진행 방법](https://www.notion.so/46dbd9440a4f4d5e97228011dff70f5a?pvs=21)
   - [참고 레포지토리](https://github.com/Leets-Official/Blog-BE-ItoR) -> 전 기수 repo입니다. 브랜치, pr 작성법, 리뷰 방법 참고하세요!
   


## 📌 1주차 목표

* 블로그 프로젝트 기본 기능 구현
  * 게시글 작성(Create)
  * 게시글 조회(Read)
* 선택 과제 (옵션)
  * 게시글 수정(Update)
  * 게시글 삭제(Delete)
  * 게시글 상세 조회

---

## 📎 실행 경로

* 글 목록 보기: [http://localhost:8080/posts](http://localhost:8080/posts)
* 새 글 작성: [http://localhost:8080/post/new](http://localhost:8080/post/new)

---

## 💾 데이터 저장소

* **HashMap** 사용하여 구현
* Key 생성 및 관리 관련 고민 포인트:
  1. **Key 생성 책임** → `?` 계층에서 관리하는 것이 적절
  2. **Key 타입** → `?` 또는 `?` 권장
  3. **중복 방지** → `?`(자동 증가) 또는 `?` 활용

---

## 🏗 MVC 구조

* **Model**: 게시글 객체 (id, title, content 등)
* **View**: 화면 렌더링 (Thymeleaf, HTML)
* **Controller**: 요청/응답 처리 (웹 요청을 받아 Service 호출)
* **Service**: 비즈니스 로직 담당
* **Repository**: 데이터 저장/조회 관리 (HashMap 기반)

👉 MVC는 **역할과 책임을 기준**으로 분리됨.

---

## 📂 프로젝트 구조
```
   ├─main
    │  ├─java
    │  │  └─com
    │  │      └─leets
    │  │          └─backend
    │  │              └─blog
    │  │                  │  BlogApplication.java
    │  │                  │
    │  │                  ├─controller
    │  │                  │      PostController.java
    │  │                  │
    │  │                  ├─model
    │  │                  │      Post.java
    │  │                  │
    │  │                  ├─repository
    │  │                  │      PostRepository.java
    │  │                  │
    │  │                  └─service
    │  │                          PostService.java
    │  │
    │  └─resources
    │      │  application.yml
    │      │
    │      └─templates
    │              new-post.html
    │              posts.html
    │
    └─test
        └─java
            └─com
                └─leets
                    └─backend
                        └─blog
                                BlogManageApplicationTests.java
```


---

## 👥 팀 구성

| 1조   | 2조   | 3조   |
| ----- | ----- | ----- |
| 김은서 | 장유정 | 김민지 |
| 김기찬 | 성현준 | 강태이 |
| 박승주 | 설지은 | 김지민 |
|       | 박소윤 |       |

---

