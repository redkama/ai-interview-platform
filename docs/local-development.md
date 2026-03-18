# 로컬 개발 가이드

## 필수 설치 항목
- Java 21
- Node.js
- Python 3
- MySQL 8
- 선택: Docker Desktop

## 서비스 구성
- `backend`: Spring Boot API 서버, 기본 포트 `8080`
- `frontend`: React + Vite 개발 서버, 기본 포트 `5173`
- `ai-service`: FastAPI 기반 AI 마이크로서비스, 기본 포트 `8000`
- `mysql`: 로컬 DB, 기본 포트 `3306`

## 환경변수 설정

### 루트 `.env.example`
- 백엔드와 AI 서비스 핵심 설정 예시가 들어 있습니다.

### backend
1. `backend/.env.example` 또는 루트 `.env.example`를 참고합니다.
2. 최소 설정
   - `DB_URL`
   - `DB_USERNAME`
   - `DB_PASSWORD`
   - `JWT_SECRET`
   - `JWT_ACCESS_EXPIRATION`
   - `JWT_REFRESH_EXPIRATION`
   - `AI_SERVICE_URL`

### frontend
1. `frontend/.env.example`를 참고합니다.
2. 최소 설정
   - `VITE_API_BASE_URL`
   - `VITE_AI_SERVICE_BASE_URL`
   - 필요 시 `VITE_USE_API_STUB`

### ai-service
1. `ai-service/.env.example`를 참고합니다.
2. 최소 설정
   - `CODEX_API_KEY`
   - `CODEX_MODEL`
   - `USE_MOCK`
   - `PORT`

## 실행 순서

### 1. MySQL 실행
```bash
docker compose up mysql
```

### 2. AI 서비스 실행
```bash
cd ai-service
pip install -r requirements.txt
uvicorn main:app --reload --port 8000
```

### 3. 백엔드 실행
```bash
cd backend
./gradlew bootRun
```

### 4. 프런트 실행
```bash
cd frontend
npm install
npm run dev
```

## 서비스 연결 관계
- 프런트는 기본적으로 백엔드 API와 통신합니다.
- 프런트의 `/ai` 요청은 로컬 개발 시 `ai-service`로 프록시됩니다.
- 백엔드는 현재 AI 서비스와 완전한 실연동 대신 일부 스텁 구조를 포함합니다.

## 자주 발생하는 오류와 해결

### DB 연결 실패
- MySQL이 실행 중인지 확인합니다.
- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`를 확인합니다.
- DB 스키마가 생성되어 있는지 확인합니다.

### JWT 관련 인증 오류
- 액세스 토큰이 만료되었을 수 있습니다.
- 브라우저 저장소를 비우고 다시 로그인합니다.

### 프런트에서 API 호출 실패
- `backend`가 `8080`에서 떠 있는지 확인합니다.
- Vite 프록시 설정과 `.env` 값을 확인합니다.

### AI 서비스 호출 실패
- `ai-service`가 `8000`에서 실행 중인지 확인합니다.
- 먼저 `USE_MOCK=true`로 검증하는 것이 안전합니다.

### 샘플 데이터가 보이지 않음
- `backend`를 `local` 프로필로 실행했는지 확인합니다.
- 샘플 시더가 이미 실행된 상태라면 대표 사용자 존재 여부를 확인합니다.
