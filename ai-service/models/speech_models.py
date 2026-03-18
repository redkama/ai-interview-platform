from pydantic import BaseModel


class SpeechPlaceholderResponse(BaseModel):
    success: bool
    data: dict
