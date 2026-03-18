from models.interview_models import InterviewFeedbackData

from chains.interview_chain import generate_interview_feedback


def run_feedback_chain(question: str, answer: str) -> InterviewFeedbackData:
    return generate_interview_feedback(question=question, answer=answer)
