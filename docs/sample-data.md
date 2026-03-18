# 샘플 데이터 안내

## 적용 방식
- 로더 클래스: `backend/src/main/java/com/aimentor/common/seed/LocalSampleDataSeeder.java`
- 실행 조건: `local` 프로필에서만 동작
- 중복 방지: 대표 샘플 사용자 존재 시 전체 시딩을 다시 수행하지 않음

## 샘플 계정
- 이메일: `demo@aimentor.dev`
- 비밀번호: `password123`

## 포함 데이터

### 영어 교육
- `BEGINNER`, `INTERMEDIATE`, `ADVANCED` 레벨별 레슨
- 레벨 테스트 결과 예시
- 영어 학습 이력 예시

### 한국사 교육
- `ANCIENT`, `THREE_KINGDOMS`, `GORYEO`, `JOSEON`, `MODERN`, `CONTEMPORARY` 시대별 레슨
- 각 레슨에 연결된 퀴즈, 정답, 해설
- 한국사 학습 이력 예시

### 도서 판매
- 카테고리별 도서 목록
- 장바구니 예시
- 주문과 주문 항목 예시

### 면접
- 샘플 사용자
- 인터뷰 세션 2개 이상
- 질문/답변/피드백 예시
- 결과 리포트용 점수 데이터

## 확인 방법
1. `backend`를 `local` 프로필로 실행합니다.
2. `demo@aimentor.dev / password123`로 로그인합니다.
3. 아래를 확인합니다.
   - 영어 레슨 목록이 보이는지
   - 한국사 레슨과 퀴즈가 보이는지
   - 도서 목록과 주문 내역이 보이는지
   - 인터뷰 세션과 리포트가 조회되는지

## 주의사항
- 일부 데이터만 삭제한 뒤 시더를 다시 돌려도 부분 복구는 하지 않습니다.
- 운영 환경에서는 자동 실행되지 않습니다.
