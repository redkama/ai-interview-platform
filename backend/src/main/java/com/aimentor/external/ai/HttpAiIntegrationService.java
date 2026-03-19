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
import org.springframework.web.client.RestClient;

@Service
@ConditionalOnProperty(prefix = "integration.ai", name = "provider", havingValue = "http-ai")
public class HttpAiIntegrationService implements AiIntegrationService {

    private final AiIntegrationProperties properties;
    private final RestClient restClient;

    public HttpAiIntegrationService(AiIntegrationProperties properties) {
        this.properties = properties;
        this.restClient = RestClient.builder()
                .baseUrl(properties.baseUrl())
                .build();
    }

    @Override
    public AiGenerateInterviewQuestionsResponse generateInterviewQuestions(AiGenerateInterviewQuestionsRequest request) {
        InterviewQuestionsApiResponse response = restClient.post()
                .uri("/ai/interview/questions")
                .body(new InterviewQuestionsApiRequest(
                        nullToEmpty(request.resumeSummary()),
                        "",
                        nullToEmpty(request.jobDescription())
                ))
                .retrieve()
                .body(InterviewQuestionsApiResponse.class);

        List<AiQuestionItem> questions = new ArrayList<>();
        List<String> questionTexts = response == null || response.data() == null ? List.of() : response.data().questions();
        for (int index = 0; index < questionTexts.size() && index < Math.max(1, request.questionCount()); index++) {
            questions.add(new AiQuestionItem(index + 1, questionTexts.get(index)));
        }

        return new AiGenerateInterviewQuestionsResponse(questions, resolveProviderName(), false);
    }

    @Override
    public AiAnalyzeAnswerFeedbackResponse analyzeAnswerFeedback(AiAnalyzeAnswerFeedbackRequest request) {
        InterviewFeedbackApiResponse response = restClient.post()
                .uri("/ai/interview/feedback")
                .body(new InterviewFeedbackApiRequest(request.questionText(), request.answerText()))
                .retrieve()
                .body(InterviewFeedbackApiResponse.class);

        InterviewFeedbackApiData data = response == null ? null : response.data();
        if (data == null) {
            throw new IllegalStateException("AI feedback response is empty.");
        }

        return new AiAnalyzeAnswerFeedbackResponse(
                data.relevance(),
                data.logic(),
                data.specificity(),
                data.overall(),
                data.comment(),
                resolveProviderName(),
                false
        );
    }

    @Override
    public AiGenerateReportSummaryResponse generateReportSummary(AiGenerateReportSummaryRequest request) {
        ReportSummaryApiResponse response = restClient.post()
                .uri("/ai/interview/report-summary")
                .body(new ReportSummaryApiRequest(
                        request.sessionTitle(),
                        request.positionTitle(),
                        request.answerFeedback() == null
                                ? List.of()
                                : request.answerFeedback().stream()
                                .map(item -> new ReportSummaryFeedbackItem(
                                        item.relevanceScore(),
                                        item.logicScore(),
                                        item.specificityScore(),
                                        item.overallScore(),
                                        item.feedbackSummary()
                                ))
                                .toList()
                ))
                .retrieve()
                .body(ReportSummaryApiResponse.class);

        ReportSummaryApiData data = response == null ? null : response.data();
        if (data == null) {
            throw new IllegalStateException("AI report summary response is empty.");
        }

        return new AiGenerateReportSummaryResponse(
                data.summary(),
                resolveProviderName(),
                false
        );
    }

    @Override
    public AiGenerateEnglishFeedbackResponse generateEnglishFeedback(AiGenerateEnglishFeedbackRequest request) {
        EnglishFeedbackApiResponse response = restClient.post()
                .uri("/ai/education/english/feedback")
                .body(new EnglishFeedbackApiRequest(
                        nullToEmpty(request.userInput()),
                        nullToEmpty(request.expectedAnswer()),
                        nullToEmpty(request.level())
                ))
                .retrieve()
                .body(EnglishFeedbackApiResponse.class);

        EnglishFeedbackApiData data = response == null ? null : response.data();
        if (data == null) {
            throw new IllegalStateException("AI english feedback response is empty.");
        }

        return new AiGenerateEnglishFeedbackResponse(
                data.is_correct(),
                data.score(),
                data.explanation(),
                data.suggestion(),
                resolveProviderName(),
                false
        );
    }

    @Override
    public AiGenerateHistoryExplanationResponse generateHistoryExplanation(AiGenerateHistoryExplanationRequest request) {
        HistoryExplanationApiResponse response = restClient.post()
                .uri("/ai/education/history/explain")
                .body(new HistoryExplanationApiRequest(
                        nullToEmpty(request.topic()),
                        nullToEmpty(request.era())
                ))
                .retrieve()
                .body(HistoryExplanationApiResponse.class);

        HistoryExplanationApiData data = response == null ? null : response.data();
        if (data == null) {
            throw new IllegalStateException("AI history explanation response is empty.");
        }

        return new AiGenerateHistoryExplanationResponse(
                data.explanation(),
                data.key_points() == null ? List.of() : data.key_points(),
                resolveProviderName(),
                false
        );
    }

    private String resolveProviderName() {
        return properties.provider() == null || properties.provider().isBlank()
                ? "http-ai"
                : properties.provider();
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private record InterviewQuestionsApiRequest(
            String resume,
            String cover_letter,
            String job_posting
    ) {
    }

    private record InterviewQuestionsApiResponse(
            boolean success,
            InterviewQuestionsApiData data
    ) {
    }

    private record InterviewQuestionsApiData(
            List<String> questions
    ) {
    }

    private record InterviewFeedbackApiRequest(
            String question,
            String answer
    ) {
    }

    private record InterviewFeedbackApiResponse(
            boolean success,
            InterviewFeedbackApiData data
    ) {
    }

    private record InterviewFeedbackApiData(
            int relevance,
            int logic,
            int specificity,
            int overall,
            String comment
    ) {
    }

    private record ReportSummaryApiRequest(
            String session_title,
            String position_title,
            List<ReportSummaryFeedbackItem> feedback_items
    ) {
    }

    private record ReportSummaryFeedbackItem(
            int relevance,
            int logic,
            int specificity,
            int overall,
            String comment
    ) {
    }

    private record ReportSummaryApiResponse(
            boolean success,
            ReportSummaryApiData data
    ) {
    }

    private record ReportSummaryApiData(
            String summary
    ) {
    }

    private record EnglishFeedbackApiRequest(
            String user_input,
            String expected_answer,
            String level
    ) {
    }

    private record EnglishFeedbackApiResponse(
            boolean success,
            EnglishFeedbackApiData data
    ) {
    }

    private record EnglishFeedbackApiData(
            boolean is_correct,
            int score,
            String explanation,
            String suggestion
    ) {
    }

    private record HistoryExplanationApiRequest(
            String topic,
            String era
    ) {
    }

    private record HistoryExplanationApiResponse(
            boolean success,
            HistoryExplanationApiData data
    ) {
    }

    private record HistoryExplanationApiData(
            String explanation,
            List<String> key_points
    ) {
    }
}
