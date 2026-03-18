from fastapi import APIRouter

from chains.english_chain import generate_english_feedback
from chains.history_chain import generate_history_explanation
from models.education_models import (
    EnglishFeedbackRequest,
    EnglishFeedbackResponse,
    HistoryExplainRequest,
    HistoryExplainResponse,
)

router = APIRouter(prefix="/ai/education", tags=["education"])


@router.post("/english/feedback", response_model=EnglishFeedbackResponse)
def create_english_feedback(request: EnglishFeedbackRequest) -> EnglishFeedbackResponse:
    data = generate_english_feedback(
        user_input=request.user_input,
        expected_answer=request.expected_answer,
        level=request.level,
    )
    return EnglishFeedbackResponse(success=True, data=data)


@router.post("/history/explain", response_model=HistoryExplainResponse)
def create_history_explanation(request: HistoryExplainRequest) -> HistoryExplainResponse:
    data = generate_history_explanation(topic=request.topic, era=request.era)
    return HistoryExplainResponse(success=True, data=data)
