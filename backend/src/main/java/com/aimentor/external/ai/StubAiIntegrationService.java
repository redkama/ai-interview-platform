package com.aimentor.external.ai;

import com.aimentor.external.ai.dto.AiAnalyzeAnswerFeedbackRequest;
import com.aimentor.external.ai.dto.AiAnalyzeAnswerFeedbackResponse;
import com.aimentor.external.ai.dto.AiGenerateEnglishFeedbackRequest;
import com.aimentor.external.ai.dto.AiGenerateEnglishFeedbackResponse;
import com.aimentor.external.ai.dto.AiGenerateHistoryExplanationRequest;
import com.aimentor.external.ai.dto.AiGenerateHistoryExplanationResponse;
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
                    request.positionTitle() + " interview question " + index
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
                "Local heuristic feedback is being used because the external AI provider is not active.",
                resolveProviderName(),
                true
        );
    }

    @Override
    public AiGenerateReportSummaryResponse generateReportSummary(AiGenerateReportSummaryRequest request) {
        int feedbackCount = request.answerFeedback() == null ? 0 : request.answerFeedback().size();

        return new AiGenerateReportSummaryResponse(
                "'" + request.sessionTitle() + "' summary generated from " + feedbackCount + " feedback items.",
                resolveProviderName(),
                true
        );
    }

    @Override
    public AiGenerateEnglishFeedbackResponse generateEnglishFeedback(AiGenerateEnglishFeedbackRequest request) {
        String normalizedInput = normalize(request.userInput());
        String normalizedExpectedAnswer = normalize(request.expectedAnswer());
        boolean correct = !normalizedInput.isBlank() && normalizedInput.equalsIgnoreCase(normalizedExpectedAnswer);
        int score = correct ? 95 : Math.max(40, 60 + similarityBonus(normalizedInput, normalizedExpectedAnswer));

        return new AiGenerateEnglishFeedbackResponse(
                correct,
                Math.min(score, 100),
                correct
                        ? "The answer matches the expected meaning closely."
                        : "The answer is understandable, but it misses part of the target expression.",
                correct
                        ? "Try expanding it with one more supporting sentence."
                        : "Reuse the key verbs and sentence structure from the expected answer.",
                resolveProviderName(),
                true
        );
    }

    @Override
    public AiGenerateHistoryExplanationResponse generateHistoryExplanation(AiGenerateHistoryExplanationRequest request) {
        return new AiGenerateHistoryExplanationResponse(
                request.topic() + " should be studied together with the political background and social changes of the "
                        + request.era() + " era.",
                List.of(
                        "Start from the era's political structure and major actors.",
                        "Connect the topic to reforms, conflict, or institutional change.",
                        "Remember one concrete outcome that continued into the next period."
                ),
                resolveProviderName(),
                true
        );
    }

    private String resolveProviderName() {
        return properties.provider() == null || properties.provider().isBlank()
                ? "stub-ai"
                : properties.provider();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().replaceAll("\\s+", " ");
    }

    private int similarityBonus(String input, String expectedAnswer) {
        if (input.isBlank() || expectedAnswer.isBlank()) {
            return 0;
        }

        int shorterLength = Math.min(input.length(), expectedAnswer.length());
        int sharedPrefixLength = 0;
        for (int index = 0; index < shorterLength; index++) {
            if (Character.toLowerCase(input.charAt(index)) != Character.toLowerCase(expectedAnswer.charAt(index))) {
                break;
            }
            sharedPrefixLength++;
        }
        return sharedPrefixLength * 3;
    }
}
