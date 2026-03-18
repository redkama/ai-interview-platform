from pydantic import BaseModel, Field


class InterviewQuestionsRequest(BaseModel):
    resume: str = Field(..., description="Resume text")
    cover_letter: str = Field(..., description="Cover letter text")
    job_posting: str = Field(..., description="Job posting text")


class InterviewQuestionsData(BaseModel):
    questions: list[str] = Field(..., min_length=5, max_length=5)


class InterviewQuestionsResponse(BaseModel):
    success: bool
    data: InterviewQuestionsData


class InterviewFeedbackRequest(BaseModel):
    question: str
    answer: str


class InterviewFeedbackData(BaseModel):
    relevance: int = Field(..., ge=0, le=100)
    logic: int = Field(..., ge=0, le=100)
    specificity: int = Field(..., ge=0, le=100)
    overall: int = Field(..., ge=0, le=100)
    comment: str


class InterviewFeedbackResponse(BaseModel):
    success: bool
    data: InterviewFeedbackData


class ReportSummaryFeedbackItem(BaseModel):
    relevance: int = Field(..., ge=0, le=100)
    logic: int = Field(..., ge=0, le=100)
    specificity: int = Field(..., ge=0, le=100)
    overall: int = Field(..., ge=0, le=100)
    comment: str


class InterviewReportSummaryRequest(BaseModel):
    session_title: str
    position_title: str | None = None
    feedback_items: list[ReportSummaryFeedbackItem] = Field(default_factory=list)


class InterviewReportSummaryData(BaseModel):
    summary: str


class InterviewReportSummaryResponse(BaseModel):
    success: bool
    data: InterviewReportSummaryData

