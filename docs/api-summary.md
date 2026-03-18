# API 요약

## 공통
- 응답 형식: `success`, `data`, `error`
- 인증 방식: JWT Bearer 토큰
- 인증이 필요한 API는 로그인 후 `Authorization: Bearer {accessToken}` 헤더가 필요합니다.

## auth

### 회원가입
- Method: `POST`
- Path: `/api/v1/auth/signup`
- 설명: 사용자 계정을 생성합니다.
- 인증: 불필요
- Request 예시
```json
{
  "name": "홍길동",
  "email": "hong@example.com",
  "password": "password123"
}
```
- Response 예시
```json
{
  "success": true,
  "data": {
    "user": {
      "id": 1,
      "name": "홍길동",
      "email": "hong@example.com",
      "role": "USER"
    },
    "accessToken": "access-token",
    "refreshToken": "refresh-token"
  },
  "error": null
}
```

### 로그인
- Method: `POST`
- Path: `/api/v1/auth/login`
- 설명: 이메일과 비밀번호로 로그인합니다.
- 인증: 불필요
- Request 예시
```json
{
  "email": "hong@example.com",
  "password": "password123"
}
```
- Response 예시: 회원가입 응답과 동일 구조

### 토큰 재발급
- Method: `POST`
- Path: `/api/v1/auth/refresh`
- 설명: 리프레시 토큰으로 액세스 토큰을 재발급합니다.
- 인증: 불필요
- Request 예시
```json
{
  "refreshToken": "refresh-token"
}
```
- Response 예시
```json
{
  "success": true,
  "data": {
    "accessToken": "new-access-token",
    "refreshToken": "new-refresh-token"
  },
  "error": null
}
```

### 로그아웃
- Method: `POST`
- Path: `/api/v1/auth/logout`
- 설명: 리프레시 토큰을 무효화합니다.
- 인증: 불필요
- Request 예시
```json
{
  "refreshToken": "refresh-token"
}
```
- Response 예시
```json
{
  "success": true,
  "data": null,
  "error": null
}
```

## profile

### 이력서 목록 조회
- Method: `GET`
- Path: `/api/v1/profiles/resumes`
- 설명: 로그인 사용자의 이력서를 조회합니다.
- 인증: 필요
- Request body 예시: 없음
- Response 예시
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "title": "백엔드 개발자 이력서",
      "content": "Spring Boot 기반 개발 경험...",
      "updatedAt": "2026-03-17T10:00:00"
    }
  ],
  "error": null
}
```

### 이력서 생성
- Method: `POST`
- Path: `/api/v1/profiles/resumes`
- 설명: 이력서를 생성합니다.
- 인증: 필요
- Request 예시
```json
{
  "title": "백엔드 개발자 이력서",
  "content": "Spring Boot 기반 개발 경험..."
}
```
- Response 예시
```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "백엔드 개발자 이력서",
    "content": "Spring Boot 기반 개발 경험..."
  },
  "error": null
}
```

### 이력서 상세 조회
- Method: `GET`
- Path: `/api/v1/profiles/resumes/{resumeId}`
- 설명: 특정 이력서를 조회합니다.
- 인증: 필요
- Request body 예시: 없음
- Response 예시: 생성 응답과 동일 구조

### 이력서 수정
- Method: `PUT`
- Path: `/api/v1/profiles/resumes/{resumeId}`
- 설명: 특정 이력서를 수정합니다.
- 인증: 필요
- Request 예시: 생성과 동일
- Response 예시: 수정된 이력서 객체

### 이력서 삭제
- Method: `DELETE`
- Path: `/api/v1/profiles/resumes/{resumeId}`
- 설명: 특정 이력서를 삭제합니다.
- 인증: 필요
- Request body 예시: 없음
- Response 예시
```json
{
  "success": true,
  "data": null,
  "error": null
}
```

### 자기소개서 CRUD
- Method: `GET/POST/PUT/DELETE`
- Path: `/api/v1/profiles/cover-letters`, `/api/v1/profiles/cover-letters/{id}`
- 설명: 로그인 사용자의 자기소개서를 관리합니다.
- 인증: 필요
- Request 예시
```json
{
  "title": "카카오뱅크 지원서",
  "companyName": "카카오뱅크",
  "content": "지원 동기와 경험..."
}
```
- Response 예시: 이력서와 유사한 CRUD 응답 구조

### 채용공고 CRUD
- Method: `GET/POST/PUT/DELETE`
- Path: `/api/v1/profiles/job-postings`, `/api/v1/profiles/job-postings/{id}`
- 설명: 로그인 사용자의 채용공고 메모를 관리합니다.
- 인증: 필요
- Request 예시
```json
{
  "companyName": "카카오뱅크",
  "positionTitle": "백엔드 엔지니어",
  "description": "플랫폼 백엔드 개발"
}
```
- Response 예시: 이력서와 유사한 CRUD 응답 구조

## interview

### 면접 세션 시작
- Method: `POST`
- Path: `/api/v1/interviews/sessions`
- 설명: 면접 세션을 생성하고 질문을 준비합니다.
- 인증: 필요
- Request 예시
```json
{
  "title": "백엔드 모의 면접",
  "positionTitle": "백엔드 엔지니어",
  "resumeId": 1,
  "coverLetterId": 1,
  "jobPostingId": 1,
  "questionCount": 3
}
```
- Response 예시
```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "백엔드 모의 면접",
    "status": "IN_PROGRESS",
    "questions": [
      {
        "id": 10,
        "content": "트래픽이 증가한 상황에서 어떤 개선을 했나요?"
      }
    ]
  },
  "error": null
}
```

### 면접 세션 상세 조회
- Method: `GET`
- Path: `/api/v1/interviews/sessions/{sessionId}`
- 설명: 세션과 질문/답변 현황을 조회합니다.
- 인증: 필요
- Request body 예시: 없음
- Response 예시: 세션 시작 응답과 유사

### 답변 저장
- Method: `POST`
- Path: `/api/v1/interviews/sessions/{sessionId}/answers`
- 설명: 특정 질문에 대한 답변을 저장합니다.
- 인증: 필요
- Request 예시
```json
{
  "questionId": 10,
  "content": "캐시 전략과 인덱스 최적화를 적용했습니다.",
  "audioUrl": null
}
```
- Response 예시
```json
{
  "success": true,
  "data": {
    "id": 100,
    "questionId": 10,
    "content": "캐시 전략과 인덱스 최적화를 적용했습니다."
  },
  "error": null
}
```

### 세션 종료
- Method: `POST`
- Path: `/api/v1/interviews/sessions/{sessionId}/end`
- 설명: 면접 세션을 종료합니다.
- 인증: 필요
- Request body 예시: 없음
- Response 예시: 종료된 세션 정보

### 결과 리포트 조회
- Method: `GET`
- Path: `/api/v1/interviews/sessions/{sessionId}/report`
- 설명: 점수와 요약 피드백이 포함된 리포트를 조회합니다.
- 인증: 필요
- Request body 예시: 없음
- Response 예시
```json
{
  "success": true,
  "data": {
    "sessionId": 1,
    "overallScore": 84,
    "strengths": ["성과 설명이 구체적입니다."],
    "weaknesses": ["트레이드오프 설명 보강 필요"]
  },
  "error": null
}
```

## education

### 영어 레슨 목록
- Method: `GET`
- Path: `/api/education/english/lessons?level=BEGINNER`
- 설명: 레벨별 영어 레슨을 조회합니다.
- 인증: 필요
- Request body 예시: 없음
- Response 예시
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "title": "기본 자기소개 만들기",
      "part": "말하기",
      "level": "BEGINNER",
      "content": "역할, 강점, 관심사 소개"
    }
  ],
  "error": null
}
```

### 영어 레슨 상세
- Method: `GET`
- Path: `/api/education/english/lessons/{id}`
- 설명: 영어 레슨 1건을 조회합니다.
- 인증: 필요
- Request body 예시: 없음
- Response 예시: 레슨 목록 항목과 동일

### 영어 레벨 테스트 결과 저장
- Method: `POST`
- Path: `/api/education/english/level-test`
- 설명: 로그인 사용자의 레벨 테스트 결과를 저장합니다.
- 인증: 필요
- Request 예시
```json
{
  "testScore": 72
}
```
- Response 예시
```json
{
  "success": true,
  "data": {
    "id": 1,
    "level": "INTERMEDIATE",
    "testScore": 72
  },
  "error": null
}
```

### 영어 학습 이력 저장
- Method: `POST`
- Path: `/api/education/english/history`
- 설명: 로그인 사용자의 영어 레슨 학습 이력을 저장합니다.
- 인증: 필요
- Request 예시
```json
{
  "lessonId": 1,
  "score": 90
}
```
- Response 예시
```json
{
  "success": true,
  "data": {
    "id": 1,
    "lessonId": 1,
    "score": 90,
    "completedAt": "2026-03-17T10:00:00"
  },
  "error": null
}
```

### 한국사 레슨 목록
- Method: `GET`
- Path: `/api/education/history/lessons?era=JOSEON`
- 설명: 시대별 한국사 레슨을 조회합니다.
- 인증: 필요
- Request body 예시: 없음
- Response 예시
```json
{
  "success": true,
  "data": [
    {
      "id": 100,
      "title": "조선의 통치 체제",
      "era": "JOSEON",
      "content": "성리학 질서와 국가 제도"
    }
  ],
  "error": null
}
```

### 한국사 레슨 상세
- Method: `GET`
- Path: `/api/education/history/lessons/{id}`
- 설명: 한국사 레슨 1건을 조회합니다.
- 인증: 필요
- Request body 예시: 없음
- Response 예시: 레슨 목록 항목과 동일

### 한국사 퀴즈 조회
- Method: `GET`
- Path: `/api/education/history/lessons/{id}/quiz`
- 설명: 특정 레슨의 퀴즈를 조회합니다.
- 인증: 필요
- Request body 예시: 없음
- Response 예시
```json
{
  "success": true,
  "data": [
    {
      "id": 200,
      "question": "조선을 건국한 인물은?",
      "options": "[\"태조\",\"세종\",\"영조\",\"정조\"]",
      "answer": "태조",
      "explanation": "태조 이성계가 조선을 건국했습니다."
    }
  ],
  "error": null
}
```

### 한국사 퀴즈 제출
- Method: `POST`
- Path: `/api/education/history/quiz/submit`
- 설명: 퀴즈 답안을 제출하고 결과를 저장합니다.
- 인증: 필요
- Request 예시
```json
{
  "lessonId": 100,
  "answers": [
    {
      "quizId": 200,
      "selectedAnswer": "태조"
    }
  ]
}
```
- Response 예시
```json
{
  "success": true,
  "data": {
    "lessonId": 100,
    "quizScore": 100,
    "results": [
      {
        "quizId": 200,
        "correct": true
      }
    ]
  },
  "error": null
}
```

### 내 한국사 학습 이력
- Method: `GET`
- Path: `/api/education/history/my-history`
- 설명: 로그인 사용자의 한국사 학습 이력을 조회합니다.
- 인증: 필요
- Request body 예시: 없음
- Response 예시: 학습 이력 배열

## book

### 도서 목록 조회
- Method: `GET`
- Path: `/api/books?category=INTERVIEW&page=0&size=12`
- 설명: 카테고리와 페이지 기준으로 도서 목록을 조회합니다.
- 인증: 필요
- Request body 예시: 없음
- Response 예시
```json
{
  "success": true,
  "data": {
    "content": [
      {
        "id": 1,
        "title": "면접 전략 플레이북",
        "author": "에이 멘토",
        "price": 22000,
        "category": "INTERVIEW"
      }
    ],
    "page": 0,
    "size": 12,
    "totalElements": 20
  },
  "error": null
}
```

### 도서 상세 조회
- Method: `GET`
- Path: `/api/books/{id}`
- 설명: 도서 상세를 조회합니다.
- 인증: 필요
- Request body 예시: 없음
- Response 예시: 목록 항목 + 설명, 재고, 이미지 URL 포함

### 도서 검색
- Method: `GET`
- Path: `/api/books/search?q=면접`
- 설명: 제목/저자/설명 기준으로 도서를 검색합니다.
- 인증: 필요
- Request body 예시: 없음
- Response 예시: 도서 배열

### 관리자 도서 등록
- Method: `POST`
- Path: `/api/admin/books`
- 설명: 관리자 권한으로 도서를 등록합니다.
- 인증: 필요
- Request 예시
```json
{
  "title": "새 도서",
  "author": "저자명",
  "publisher": "출판사",
  "price": 18000,
  "stock": 10,
  "description": "도서 설명",
  "imageUrl": "https://example.com/book.jpg",
  "category": "INTERVIEW"
}
```
- Response 예시: 생성된 도서 객체

### 장바구니 조회
- Method: `GET`
- Path: `/api/cart`
- 설명: 로그인 사용자의 장바구니를 조회합니다.
- 인증: 필요
- Request body 예시: 없음
- Response 예시
```json
{
  "success": true,
  "data": {
    "id": 1,
    "items": [
      {
        "id": 1,
        "bookId": 1,
        "title": "면접 전략 플레이북",
        "quantity": 2,
        "price": 22000,
        "linePrice": 44000
      }
    ],
    "totalPrice": 44000
  },
  "error": null
}
```

### 장바구니 항목 추가
- Method: `POST`
- Path: `/api/cart/items`
- 설명: 장바구니에 도서를 추가합니다.
- 인증: 필요
- Request 예시
```json
{
  "bookId": 1,
  "quantity": 2
}
```
- Response 예시: 장바구니 조회와 동일 구조

### 장바구니 항목 수량 수정
- Method: `PATCH`
- Path: `/api/cart/items/{id}`
- 설명: 장바구니 항목 수량을 수정합니다.
- 인증: 필요
- Request 예시
```json
{
  "quantity": 3
}
```
- Response 예시: 장바구니 조회와 동일 구조

### 장바구니 항목 삭제
- Method: `DELETE`
- Path: `/api/cart/items/{id}`
- 설명: 장바구니 항목을 삭제합니다.
- 인증: 필요
- Request body 예시: 없음
- Response 예시: 장바구니 조회와 동일 구조

### 주문 생성
- Method: `POST`
- Path: `/api/orders`
- 설명: 장바구니를 기준으로 주문을 생성합니다.
- 인증: 필요
- Request body 예시: 없음
- Response 예시
```json
{
  "success": true,
  "data": {
    "id": 100,
    "status": "PENDING",
    "totalPrice": 44000,
    "orderedAt": "2026-03-17T10:00:00"
  },
  "error": null
}
```

### 내 주문 목록
- Method: `GET`
- Path: `/api/orders/my`
- 설명: 로그인 사용자의 주문 목록을 조회합니다.
- 인증: 필요
- Request body 예시: 없음
- Response 예시: 주문 배열

### 주문 상세 조회
- Method: `GET`
- Path: `/api/orders/{id}`
- 설명: 본인 주문 상세를 조회합니다.
- 인증: 필요
- Request body 예시: 없음
- Response 예시: 주문 1건 상세

## report / dashboard

### 면접 리포트
- Method: `GET`
- Path: `/api/v1/interviews/sessions/{sessionId}/report`
- 설명: 대시보드/결과 화면에서 사용하는 면접 리포트입니다.
- 인증: 필요
- Request body 예시: 없음
- Response 예시: 위 인터뷰 리포트와 동일

### 대시보드 전용 API
- 현재 전용 대시보드 집계 API는 없습니다.
- 프런트는 면접 결과, 교육 진행률, 주문 목록을 조합하거나 스텁 데이터를 사용합니다.
