from pydantic import BaseModel, Field


class EnglishFeedbackRequest(BaseModel):
    user_input: str
    expected_answer: str
    level: str


class EnglishFeedbackData(BaseModel):
    is_correct: bool
    score: int = Field(..., ge=0, le=100)
    explanation: str
    suggestion: str


class EnglishFeedbackResponse(BaseModel):
    success: bool
    data: EnglishFeedbackData


class HistoryExplainRequest(BaseModel):
    topic: str
    era: str


class HistoryExplainData(BaseModel):
    explanation: str
    key_points: list[str]


class HistoryExplainResponse(BaseModel):
    success: bool
    data: HistoryExplainData
