# 백엔드 설정

## 패키지 구조

백엔드는 `com.aimentor`를 루트로 사용하는 기능 중심 구조를 따릅니다.

```text
com.aimentor
|- BackendApplication
|- common
|  |- api
|  |  \- ApiResponse
|  |- config
|  |  \- SecurityConfig
|  |- entity
|  |  \- BaseTimeEntity
|  \- exception
|     \- GlobalExceptionHandler
|- domain.user
|- domain.profile
|- domain.interview
|- domain.education
|- domain.book
|- external.ai
\- external.speech
```

## 구조 메모
- `common`에는 공통 응답, 보안, 예외 처리와 같은 기반 코드가 있습니다.
- `domain.*`는 기능별로 `controller`, `service`, `repository`, `dto`, `entity`를 분리합니다.
- `external.*`는 AI, 음성 등 외부 연동 영역입니다.

## 로컬 실행 준비

### 1. MySQL 데이터베이스 생성
```sql
CREATE DATABASE aimentor CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 환경변수 설정
`backend/.env.example` 또는 루트 `.env.example`를 참고해 값을 준비합니다.

예시:
```env
DB_URL=jdbc:mysql://localhost:3306/aimentor?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
DB_USERNAME=root
DB_PASSWORD=your_password
JWT_SECRET=your_generated_secret
JWT_ACCESS_EXPIRATION=3600000
JWT_REFRESH_EXPIRATION=1209600000
AI_SERVICE_URL=http://localhost:8000
SPRING_PROFILES_ACTIVE=local
```

### 3. JWT 시크릿 생성
PowerShell 예시:
```powershell
[Convert]::ToBase64String((1..64 | ForEach-Object { Get-Random -Maximum 256 } | ForEach-Object { [byte]$_ }))
```

### 4. 백엔드 실행
```powershell
cd backend
.\gradlew.bat bootRun
```

## 기본 URL
- 백엔드 API: `http://localhost:8080`

## 참고 사항
- 샘플 데이터는 `local` 프로필에서만 자동 시딩됩니다.
- AI와 음성 기능은 일부 스텁 또는 placeholder 단계가 포함되어 있습니다.
- DB 접속 정보가 맞지 않으면 애플리케이션이 시작 단계에서 실패할 수 있습니다.
