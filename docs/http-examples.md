# HTTP 예시 요청

아래 예시는 로컬 백엔드 `http://localhost:8080` 기준입니다.

## 회원가입
```bash
curl -X POST http://localhost:8080/api/v1/auth/signup \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"홍길동\",\"email\":\"hong@example.com\",\"password\":\"password123\"}"
```

## 로그인
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"demo@aimentor.dev\",\"password\":\"password123\"}"
```

## 이력서 등록
```bash
curl -X POST http://localhost:8080/api/v1/profiles/resumes \
  -H "Authorization: Bearer {ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"백엔드 개발자 이력서\",\"content\":\"Spring Boot 기반 개발 경험\"}"
```

## 면접 세션 시작
```bash
curl -X POST http://localhost:8080/api/v1/interviews/sessions \
  -H "Authorization: Bearer {ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d "{\"title\":\"백엔드 모의 면접\",\"positionTitle\":\"백엔드 엔지니어\",\"resumeId\":1,\"coverLetterId\":1,\"jobPostingId\":1,\"questionCount\":3}"
```

## 면접 답변 저장
```bash
curl -X POST http://localhost:8080/api/v1/interviews/sessions/1/answers \
  -H "Authorization: Bearer {ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d "{\"questionId\":10,\"content\":\"캐시 전략과 인덱스 최적화를 적용했습니다.\",\"audioUrl\":null}"
```

## 영어 레슨 목록 조회
```bash
curl -X GET "http://localhost:8080/api/education/english/lessons?level=BEGINNER" \
  -H "Authorization: Bearer {ACCESS_TOKEN}"
```

## 한국사 퀴즈 제출
```bash
curl -X POST http://localhost:8080/api/education/history/quiz/submit \
  -H "Authorization: Bearer {ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d "{\"lessonId\":107,\"answers\":[{\"quizId\":201,\"selectedAnswer\":\"태조\"}]}"
```

## 도서 목록 조회
```bash
curl -X GET "http://localhost:8080/api/books?category=INTERVIEW&page=0&size=12" \
  -H "Authorization: Bearer {ACCESS_TOKEN}"
```

## 장바구니 항목 추가
```bash
curl -X POST http://localhost:8080/api/cart/items \
  -H "Authorization: Bearer {ACCESS_TOKEN}" \
  -H "Content-Type: application/json" \
  -d "{\"bookId\":1,\"quantity\":2}"
```

## 주문 생성
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer {ACCESS_TOKEN}"
```
