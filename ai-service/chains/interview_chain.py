import os

from langchain_core.prompts import ChatPromptTemplate
from langchain_openai import ChatOpenAI

from models.interview_models import (
    InterviewFeedbackData,
    InterviewQuestionsData,
    InterviewReportSummaryData,
)


def generate_interview_questions(resume: str, cover_letter: str, job_posting: str) -> InterviewQuestionsData:
    if _use_mock():
        return InterviewQuestionsData(
            questions=[
                "Tell me about the experience that best matches this role.",
                "What was the hardest problem in your recent project and how did you solve it?",
                "Why do you want this position at this company?",
                "Describe a conflict in teamwork and how you handled it.",
                "What result would you want to deliver in your first three months?",
            ]
        )

    prompt = ChatPromptTemplate.from_messages(
        [
            (
                "system",
                "You are an interview question generator. Return exactly five practical interview questions.",
            ),
            (
                "human",
                "Resume:\n{resume}\n\nCover Letter:\n{cover_letter}\n\nJob Posting:\n{job_posting}",
            ),
        ]
    )
    llm = _get_llm()
    structured_llm = llm.with_structured_output(InterviewQuestionsData)
    return structured_llm.invoke(
        prompt.format_messages(
            resume=resume,
            cover_letter=cover_letter,
            job_posting=job_posting,
        )
    )


def generate_interview_feedback(question: str, answer: str) -> InterviewFeedbackData:
    if _use_mock():
        return InterviewFeedbackData(
            relevance=84,
            logic=80,
            specificity=78,
            overall=81,
            comment="The answer is relevant, but it needs clearer evidence and more concrete outcomes.",
        )

    prompt = ChatPromptTemplate.from_messages(
        [
            (
                "system",
                "You are an interview evaluator. Score relevance, logic, specificity, and overall from 0 to 100.",
            ),
            ("human", "Question:\n{question}\n\nAnswer:\n{answer}"),
        ]
    )
    llm = _get_llm()
    structured_llm = llm.with_structured_output(InterviewFeedbackData)
    return structured_llm.invoke(prompt.format_messages(question=question, answer=answer))


def generate_report_summary(
    session_title: str,
    position_title: str | None,
    feedback_items: list[dict],
) -> InterviewReportSummaryData:
    if _use_mock():
        average_score = 0
        if feedback_items:
            average_score = round(sum(item["overall"] for item in feedback_items) / len(feedback_items))
        return InterviewReportSummaryData(
            summary=(
                f"'{session_title}' 세션은 평균 {average_score}점 수준입니다. "
                "답변의 관련성은 좋지만, 성과 수치와 구체적인 근거를 조금 더 보강하면 더 설득력 있는 면접 답변이 됩니다."
            )
        )

    prompt = ChatPromptTemplate.from_messages(
        [
            (
                "system",
                "You are an interview coach. Summarize the candidate's performance in 3-4 sentences using the provided feedback items.",
            ),
            (
                "human",
                "Session title: {session_title}\nPosition title: {position_title}\nFeedback items: {feedback_items}",
            ),
        ]
    )
    llm = _get_llm()
    structured_llm = llm.with_structured_output(InterviewReportSummaryData)
    return structured_llm.invoke(
        prompt.format_messages(
            session_title=session_title,
            position_title=position_title or "",
            feedback_items=str(feedback_items),
        )
    )


def _use_mock() -> bool:
    return os.getenv("USE_MOCK", "true").lower() == "true"


def _get_llm() -> ChatOpenAI:
    api_key = os.getenv("CODEX_API_KEY", "")
    model = os.getenv("CODEX_MODEL", "gpt-4.1-mini")
    if not api_key:
        raise ValueError("CODEX_API_KEY is required when USE_MOCK=false.")
    return ChatOpenAI(api_key=api_key, model=model)

