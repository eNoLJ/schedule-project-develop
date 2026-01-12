# Schedule Project
## 프로젝트 소개
Schedule Project는 Spring Boot와 JPA를 활용하여 구현한 일정 관리 REST API 프로젝트이다.

회원 가입과 로그인 기능을 기반으로, 사용자는 일정을 생성·조회·수정·삭제할 수 있으며 각 일정에 댓글을 작성할 수 있도록 설계하였다.

단순한 CRUD 구현에 그치지 않고,

- 세션 기반 인증
- 트랜잭션 처리
- JPA Auditing
- 계층 분리
- 비즈니스 로직 검증
- 조회 성능(N+1 문제) 개선

등을 중심으로 학습하고 구현하는 것을 목표로 진행하였다.

---

## 기술 스택
- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- Spring Validation
- Session 기반 인증
- Lombok

---

## 프로젝트 구조
본 프로젝트는 역할과 책임을 명확히 하기 위해 다음과 같이 계층을 분리하였다.

### Controller
- 클라이언트 요청 수신
- HTTP 요청/응답 처리
- 인증 정보(SessionUser) 전달
- 비즈니스 로직은 Service 계층에 위임
### Service
- 핵심 비즈니스 로직 처리
- 트랜잭션 관리
- 인증/권한 및 도메인 규칙 검증
### Entity
- 데이터베이스 테이블과 매핑되는 도메인 객체
- JPA Auditing을 활용한 생성일/수정일 공통 관리
### Repository
- 데이터 접근 계층
- JPA를 통한 CRUD 처리 및 커스텀 조회
### DTO
- 요청(Request) / 응답(Response) 전용 객체
- 엔티티와 API 스펙 분리
- 비밀번호 등 민감 정보 노출 방지

---

## 인증 및 사용자 관리
### 회원가입
- 이메일 중복 여부를 검증한 후 회원가입 진행
- 비밀번호는 PasswordEncoder를 사용하여 암호화 후 저장
- API 응답에는 비밀번호를 포함하지 않음
### 로그인
- 이메일로 사용자를 조회
- 입력한 비밀번호와 저장된 암호화 비밀번호를 matches()로 비교
- 로그인 성공 시 사용자 정보를 **세션(Session)**에 저장
### 로그아웃
- 현재 세션을 무효화하여 로그아웃 처리
- 사용자 조회
- 전체 사용자 목록 조회
- 수정일 기준 내림차순 정렬
- 로그인 사용자는 자신의 정보를 단건 조회 가능
### 사용자 정보 수정
- 로그인 사용자만 수정 가능
- 현재 비밀번호 검증 후 수정 진행
- 비밀번호 변경 시 다시 암호화하여 저장
- 수정일은 수정 시점으로 자동 갱신
### 사용자 삭제
- 로그인 사용자 본인만 삭제 가능
- 비밀번호 검증 후 삭제 처리

---

## 일정 관리 기능
### 일정 생성
- 로그인한 사용자를 작성자로 하여 일정 생성
- 일정 제목, 내용 포함
- 일정 고유 식별자(ID)는 자동 생성
- 작성일/수정일은 날짜·시간을 포함하여 저장
- 작성일/수정일은 JPA Auditing으로 관리
- API 응답에서 비밀번호 제외
### 일정 조회
#### 전체 일정 조회
- 작성자명(author)을 기준으로 일정 목록 조회 가능 (선택 조건)
- 작성자 조건 유무에 따라 하나의 API로 처리
- 수정일 기준 내림차순 정렬
- 페이징 처리 지원
- 일정과 함께 댓글 개수를 조회
- 댓글 개수 조회 시 N+1 문제를 한방 쿼리로 개선
#### 선택 일정 조회
- 일정 ID를 이용한 단건 조회
- 해당 일정에 등록된 댓글 목록을 함께 응답
- API 응답에서 비밀번호 제외
### 일정 수정
- 로그인 사용자와 작성자가 일치할 경우에만 수정 가능
- 일정 제목 및 내용 수정 가능
- 수정 요청 시 비밀번호 검증
- 작성일은 유지, 수정일은 수정 시점으로 갱신
### 일정 삭제
- 로그인 사용자와 작성자가 일치할 경우에만 삭제 가능
- 비밀번호 검증 후 삭제 처리

---

## 댓글 기능
### 댓글 생성
- 로그인한 사용자가 특정 일정에 댓글 작성
- 댓글 내용 및 작성자 정보 저장
- 댓글 고유 식별자(ID)는 자동 생성
- 작성일/수정일은 JPA Auditing으로 관리
- 하나의 일정에는 최대 10개의 댓글만 작성 가능
- API 응답에서 비밀번호 제외
### 댓글 조회
- 특정 일정에 등록된 댓글 목록 조회
- 일정 ID 기준 조회

---

## RED
<details>
    <summary>RED</summary>
<img width="737" height="1080" alt="Image" src="https://github.com/user-attachments/assets/f5eae003-29c7-408e-9aa9-2af862d0d151" />
</details>

## API 명세서

<details>
    <summary>User API</summary>

### 1. 회원가입
POST `http://localhost:8080/users`

#### Request body
````
{
    "name": "정인호",
    "email": "eno@gmail.com",
    "password": "12345678"
}
````
#### Response header
````
status code 201 CREATED
````

#### Response body
````
{
    "createdAt": "2026-01-11T19:17:54.531984",
    "email": "eno@gmail.com",
    "id": 1,
    "modifiedAt": "2026-01-11T19:17:54.531984",
    "name": "정인호"
}
````

### 2. 로그인
POST `http://localhost:8080/login`

#### Request body
````
{
    "email": "eno@gmail.com",
    "password": "12345678"
}
````
#### Response header
````
status code 200 OK
````

#### Response body
````
{
    "email": "eno@gmail.com",
    "id": 1,
}
````

### 3. 로그아웃
POST `http://localhost:8080/logout`

#### Request header
````
Cookie | JSESSIONID=..........
````
#### Response header
````
status code 204 NO CONTENT
````

### 4. 유저 전체 조회
GET `http://localhost:8080/users`

#### Response header
````
status code 200 OK
````

#### Response body
````
[
    {
        "createdAt": "2026-01-12T11:37:34.917229",
        "email": "eno@gmail.com",
        "id": 1,
        "modifiedAt": "2026-01-12T11:37:34.917229",
        "name": "정인호"
    }
]
````

### 5. 유저 단건 조회
GET `http://localhost:8080/users/1`

#### Path variable
userId | 유저 id

#### Request header
````
Cookie | JSESSIONID=..........
````

#### Response header
````
status code 200 OK
````

#### Response body
````
{
    "createdAt": "2026-01-12T11:37:34.917229",
    "email": "eno@gmail.com",
    "id": 1,
    "modifiedAt": "2026-01-12T11:37:34.917229",
    "name": "정인호"
}
````

### 6. 유저 수정
PATCH `http://localhost:8080/users`

#### Request header
````
Cookie | JSESSIONID=..........
````

#### Request body
````
{
    "name": "정호인",
    "currentPassword": "12345678",
    "newPassword": "87654321"
}
````

#### Response header
````
status code 200 OK
````

#### Response body
````
{
    "createdAt": "2026-01-11T18:54:55.884283",
    "email": "eno@gmail.com",
    "id": 1,
    "modifiedAt": "2026-01-11T18:54:59.804578",
    "name": "정호인"
}
````

### 7. 유저 삭제
DELETE `http://localhost:8080/users`

#### Request header
````
Cookie | JSESSIONID=..........
````

#### Request body
````
{
    "password": "12345678"
}
````

#### Response header
````
status code 204 NO CONTENT
````

</details>







<details>
    <summary>Schedule API</summary>

### 1. 일정 생성

POST `http://localhost:8080/schedules`

#### Request header
````
Cookie | JSESSIONID=..........
````

#### Request body
````
{
    "title": "일정",
    "description": "내용"
}
````
#### Response header
````
status code 201 CREATED
````

#### Response body
````
{
    "author": "정인호",
    "createdAt": "2026-01-11T19:18:00.638106",
    "description": "내용",
    "id": 2,
    "modifiedAt": "2026-01-11T19:18:00.638106",
    "title": "일정"
}
````

### 2. 일정 전체 조회

GET `http://localhost:8080/schedules`

#### Query params
author  |   작성자

page    |   1

size    |   2

#### Response header
````
status code 200 OK
````

#### Response body
````
[
    {
        "author": "정인호",
        "commentCount": 0,
        "createdAt": "2026-01-11T19:18:00.638106",
        "id": 2,
        "modifiedAt": "2026-01-11T19:18:00.638106",
        "title": "일정"
    },
    {
        "author": "정인호",
        "commentCount": 3,
        "createdAt": "2026-01-11T19:17:59.611456",
        "id": 1,
        "modifiedAt": "2026-01-11T19:17:59.611456",
        "title": "일정"
    }
]
````

### 3. 일정 단건 조회

GET `http://localhost:8080/schedules/1`

#### Path variable

scheduleId  |   스케줄 id

#### Response header
````
status code 200 OK
````

#### Response body
````
{
    "author": "정인호",
    "createdAt": "2026-01-12T11:57:32.685374",
    "description": "내용",
    "id": 1,
    "modifiedAt": "2026-01-12T11:57:32.685374",
    "title": "일정"
}
````

### 4. 일정 수정

PATCH `http://localhost:8080/schedules/1`

#### Request header
````
Cookie | JSESSIONID=..........
````

#### Path variable

scheduleId | 스케줄 id

#### Request body
````
{
    "title": "일정 수정",
    "description": "일정 내용 수정"
}
````

#### Response header
````
status code 200 OK
````

#### Response body
````
{
    "author": "정인호",
    "createdAt": "2026-01-12T11:57:32.685374",
    "description": "일정 내용 수정",
    "id": 1,
    "modifiedAt": "2026-01-12T11:58:55.878062",
    "title": "일정 수정"
}
````

### 5. 일정 삭제

DELETE `http://localhost:8080/schedules/1`

#### Path variable

scheduleId | 스케줄 id

#### Request header
````
Cookie | JSESSIONID=..........
````

#### Response header
````
status code 204 NO CONTENT
````

</details>


<details>
    <summary>Comment API</summary>

### 1. 댓글 생성

POST `http://localhost:8080/schedules/1/comments`

#### Request header
````
Cookie | JSESSIONID=..........
````

#### Path variable

scheduleId | 스케줄 id

#### Request body
````
{
    "description": "코멘트"
}
````

#### Response header
````
status code 201 CREATED
````

#### Response body
````
{
    "createdAt": "2026-01-12T12:05:36.739818",
    "description": "코멘트",
    "id": 1,
    "modifiedAt": "2026-01-12T12:05:36.739818"
}
````

### 2. 댓글 조회

GET `http://localhost:8080/schedules/1/comments`

#### Path variable

scheduleId | 스케줄 id

#### Response header
````
status code 200 OK
````

#### Response body
````
[
    {
        "author": "정인호",
        "createdAt": "2026-01-12T12:05:36.739818",
        "description": "코멘트",
        "id": 1,
        "modifiedAt": "2026-01-12T12:05:36.739818"
    }
]
````
</details>
