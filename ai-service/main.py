import os

from dotenv import load_dotenv
from fastapi import FastAPI

from routers.education import router as education_router
from routers.interview import router as interview_router
from routers.speech import router as speech_router

load_dotenv()

app = FastAPI(
    title="AI Service",
    description="FastAPI + LangChain based AI microservice for interview and education flows.",
    version="0.1.0",
)

app.include_router(interview_router)
app.include_router(education_router)
app.include_router(speech_router)


@app.get("/health")
def health_check() -> dict:
    return {
        "success": True,
        "data": {
            "status": "ok",
            "use_mock": os.getenv("USE_MOCK", "true").lower() == "true",
        },
    }
