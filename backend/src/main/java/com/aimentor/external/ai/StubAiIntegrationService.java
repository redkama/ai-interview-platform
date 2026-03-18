package com.aimentor.external.ai;

import com.aimentor.external.ai.dto.AiAnalyzeAnswerFeedbackRequest;
import com.aimentor.external.ai.dto.AiAnalyzeAnswerFeedbackResponse;
import com.aimentor.external.ai.dto.AiGenerateInterviewQuestionsRequest;
import com.aimentor.external.ai.dto.AiGenerateInterviewQuestionsResponse;
import com.aimentor.external.ai.dto.AiGenerateReportSummaryRequest;
import com.aimentor.external.ai.dto.AiGenerateReportSummaryResponse;
import com.aimentor.external.ai.dto.AiQuestionItem;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "integration.ai", name = "provider", havingValue = "stub-ai", matchIfMissing = true)
public class StubAiIntegrationService implements AiIntegrationService {

    private final AiIntegrationProperties properties;

    public StubAiIntegrationService(AiIntegrationProperties properties) {
        this.properties = properties;
    }

    @Override
    public AiGenerateInterviewQuestionsResponse generateInterviewQuestions(AiGenerateInterviewQuestionsRequest request) {
        List<AiQuestionItem> questions = new ArrayList<>();
        int questionCount = Math.max(1, request.questionCount());
        for (int index = 1; index <= questionCount; index++) {
            questions.add(new AiQuestionItem(
                    index,
                    request.positionTitle() + " 지원자를 위한 기본 질문 " + index + "입니다."
            ));
        }

        return new AiGenerateInterviewQuestionsResponse(questions, resolveProviderName(), true);
    }

    @Override
    public AiAnalyzeAnswerFeedbackResponse analyzeAnswerFeedback(AiAnalyzeAnswerFeedbackRequest request) {
        int answerLength = request.answerText() == null ? 0 : request.answerText().trim().length();
        int relevanceScore = Math.min(100, 50 + (answerLength / 20));
        int logicScore = Math.min(100, 45 + (answerLength / 25));
        int specificityScore = Math.min(100, 40 + (answerLength / 18));
        int overallScore = Math.round((relevanceScore + logicScore + specificityScore) / 3.0f);

        return new AiAnalyzeAnswerFeedbackResponse(
                relevanceScore,
                logicScore,
                specificityScore,
                overallScore,
                "로컬 휴리스틱 기반 피드백입니다. 실제 AI 분석을 연결하면 더 정교한 코칭을 받을 수 있습니다.",
                resolveProviderName(),
                true
        );
    }

    @Override
    public AiGenerateReportSummaryResponse generateReportSummary(AiGenerateReportSummaryRequest request) {
        int feedbackCount = request.answerFeedback() == null ? 0 : request.answerFeedback().size();

        return new AiGenerateReportSummaryResponse(
                "'" + request.sessionTitle() + "' 세션의 답변 " + feedbackCount + "개를 바탕으로 만든 기본 요약입니다.",
                resolveProviderName(),
                true
        );
    }

    private String resolveProviderName() {
        return properties.provider() == null || properties.provider().isBlank()
                ? "stub-ai"
                : properties.provider();
    }
}
