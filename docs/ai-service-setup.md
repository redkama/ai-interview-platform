# AI 서비스 설정 가이드

## 개요
- 위치: `ai-service/`
- 프레임워크: FastAPI
- 기본 모드: `USE_MOCK=true`
- 문서 확인: `http://localhost:8000/docs`

## 필수 환경변수
- `CODEX_API_KEY`: 실제 Codex API 호출 시 사용
- `CODEX_MODEL`: 사용할 모델 이름
- `USE_MOCK`: `true`면 샘플 응답 반환
- `PORT`: 기본 포트, 예시는 `8000`

## 실행 방법
```bash
cd ai-service
pip install -r requirements.txt
uvicorn main:app --reload --port 8000
```

## 동작 방식
- `USE_MOCK=true`
  - 실제 모델 호출 없이 샘플 JSON을 반환합니다.
- `USE_MOCK=false`
  - 각 체인 파일에서 `langchain-openai`를 통해 Codex 호출을 시도합니다.
  - 실제 호출 위치는 `chains/*.py` 주석으로 표시되어 있습니다.

## 주요 엔드포인트
- `POST /ai/interview/questions`
- `POST /ai/interview/feedback`
- `POST /ai/education/english/feedback`
- `POST /ai/education/history/explain`
- `GET /health`

## 연동 메모
- Spring Boot에서는 HTTP JSON 클라이언트로 직접 호출하면 됩니다.
- 응답은 `success`, `data` 구조를 유지해 백엔드와 프런트에서 다루기 쉽습니다.
