import os

from langchain_core.prompts import ChatPromptTemplate
from langchain_openai import ChatOpenAI

from models.education_models import HistoryExplainData


def generate_history_explanation(topic: str, era: str) -> HistoryExplainData:
    if _use_mock():
        return HistoryExplainData(
            explanation="The topic can be explained as a major historical development that shaped institutions, culture, and political order in that era.",
            key_points=[
                "It should be understood in the context of the era's political structure.",
                "It influenced later social and cultural developments.",
                "It is often remembered through a few defining reforms or events.",
            ],
        )

    prompt = ChatPromptTemplate.from_messages(
        [
            (
                "system",
                "You are a Korean history tutor. Explain the requested topic clearly and extract key points.",
            ),
            ("human", "Era: {era}\nTopic: {topic}"),
        ]
    )
    llm = _get_llm()
    structured_llm = llm.with_structured_output(HistoryExplainData)
    # Actual Codex API integration happens here through LangChain's ChatOpenAI client.
    return structured_llm.invoke(prompt.format_messages(topic=topic, era=era))


def _use_mock() -> bool:
    return os.getenv("USE_MOCK", "true").lower() == "true"


def _get_llm() -> ChatOpenAI:
    api_key = os.getenv("CODEX_API_KEY", "")
    model = os.getenv("CODEX_MODEL", "gpt-4.1-mini")
    if not api_key:
        raise ValueError("CODEX_API_KEY is required when USE_MOCK=false.")
    return ChatOpenAI(api_key=api_key, model=model)
