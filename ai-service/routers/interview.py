from fastapi import APIRouter

from chains.feedback_chain import run_feedback_chain
from chains.interview_chain import generate_interview_questions, generate_report_summary
from models.interview_models import (
    InterviewFeedbackRequest,
    InterviewFeedbackResponse,
    InterviewQuestionsRequest,
    InterviewQuestionsResponse,
    InterviewReportSummaryRequest,
    InterviewReportSummaryResponse,
)

router = APIRouter(prefix="/ai/interview", tags=["interview"])


@router.post("/questions", response_model=InterviewQuestionsResponse)
def create_interview_questions(request: InterviewQuestionsRequest) -> InterviewQuestionsResponse:
    data = generate_interview_questions(
        resume=request.resume,
        cover_letter=request.cover_letter,
        job_posting=request.job_posting,
    )
    return InterviewQuestionsResponse(success=True, data=data)


@router.post("/feedback", response_model=InterviewFeedbackResponse)
def create_interview_feedback(request: InterviewFeedbackRequest) -> InterviewFeedbackResponse:
    data = run_feedback_chain(question=request.question, answer=request.answer)
    return InterviewFeedbackResponse(success=True, data=data)


@router.post("/report-summary", response_model=InterviewReportSummaryResponse)
def create_interview_report_summary(
    request: InterviewReportSummaryRequest,
) -> InterviewReportSummaryResponse:
    data = generate_report_summary(
        session_title=request.session_title,
        position_title=request.position_title,
        feedback_items=[item.model_dump() for item in request.feedback_items],
    )
    return InterviewReportSummaryResponse(success=True, data=data)

