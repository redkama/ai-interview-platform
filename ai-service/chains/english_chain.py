import os

from langchain_core.prompts import ChatPromptTemplate
from langchain_openai import ChatOpenAI

from models.education_models import EnglishFeedbackData


def generate_english_feedback(user_input: str, expected_answer: str, level: str) -> EnglishFeedbackData:
    if _use_mock():
        return EnglishFeedbackData(
            is_correct=True,
            score=88,
            explanation="The core meaning is correct and the sentence is understandable.",
            suggestion="Add one more concrete example and use a smoother transition phrase.",
        )

    prompt = ChatPromptTemplate.from_messages(
        [
            (
                "system",
                "You evaluate English learning answers. Compare the student's answer with the expected answer and return a concise coaching result.",
            ),
            (
                "human",
                "Level: {level}\nExpected Answer: {expected_answer}\nUser Input: {user_input}",
            ),
        ]
    )
    llm = _get_llm()
    structured_llm = llm.with_structured_output(EnglishFeedbackData)
    # Actual Codex API integration happens here through LangChain's ChatOpenAI client.
    return structured_llm.invoke(
        prompt.format_messages(
            user_input=user_input,
            expected_answer=expected_answer,
            level=level,
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
