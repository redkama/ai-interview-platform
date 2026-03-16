import { create } from 'zustand'

const sampleQuestions = [
  {
    id: 'question-1',
    prompt: '신뢰성이나 성능을 개선했던 백엔드 프로젝트 경험을 설명해 주세요.',
    intent: '주도성, 기술 깊이, 정량적 성과를 평가합니다.',
  },
  {
    id: 'question-2',
    prompt: '프론트엔드와 백엔드 팀이 함께 일하기 쉬운 API는 어떻게 설계하시나요?',
    intent: '커뮤니케이션, API 설계 습관, 협업 역량을 평가합니다.',
  },
  {
    id: 'question-3',
    prompt: '운영 장애가 발생했을 때 어떤 방식으로 조사하고 대응할지 설명해 주세요.',
    intent: '우선순위 판단, 디버깅 흐름, 장애 커뮤니케이션을 확인합니다.',
  },
]

const defaultResult = {
  overallScore: 84,
  strengths: [
    '성과를 구체적인 결과로 설명했습니다.',
    '답변에서 엔지니어링 주도성이 분명하게 드러났습니다.',
  ],
  weaknesses: [
    '일부 답변은 더 간결한 구조가 필요합니다.',
    '리스크와 트레이드오프 설명을 더 명확히 할 수 있습니다.',
  ],
  summaries: [
    {
      questionId: 'question-1',
      question: sampleQuestions[0].prompt,
      score: 86,
      summary: '결과가 포함된 좋은 예시였지만 설명은 조금 더 짧아질 수 있습니다.',
    },
    {
      questionId: 'question-2',
      question: sampleQuestions[1].prompt,
      score: 82,
      summary: '협업 관점은 좋았지만 API 버전 관리 설명은 다소 약했습니다.',
    },
    {
      questionId: 'question-3',
      question: sampleQuestions[2].prompt,
      score: 84,
      summary: '우선순위와 커뮤니케이션이 포함된 탄탄한 장애 대응 흐름이었습니다.',
    },
  ],
}

const recentInterviewSessions = [
  {
    id: 'session-2026-03-13',
    date: '2026-03-13',
    mode: '기술',
    score: 84,
    summary: '장애 대응과 주도성 사례가 강점으로 보였습니다.',
  },
  {
    id: 'session-2026-03-10',
    date: '2026-03-10',
    mode: '인성',
    score: 79,
    summary: '스토리 전달은 좋았지만 성과 지표가 부족했습니다.',
  },
  {
    id: 'session-2026-03-07',
    date: '2026-03-07',
    mode: '일반',
    score: 76,
    summary: '기본 답변은 좋았지만 구조를 더 다듬을 여지가 있습니다.',
  },
  {
    id: 'session-2026-03-03',
    date: '2026-03-03',
    mode: '기술',
    score: 72,
    summary: '트레이드오프와 디버깅 과정 설명이 더 필요했습니다.',
  },
]

const scoreTrend = recentInterviewSessions
  .slice()
  .reverse()
  .map((session) => ({
    date: session.date,
    score: session.score,
  }))

const weaknessTags = [
  '답변 구조',
  '지표 명확성',
  '트레이드오프 설명',
  'API 버전 관리 설명',
]

const recommendedNextActions = [
  '정량적 결과가 포함된 STAR 답변을 한 번 연습해 보세요.',
  'API 설계 사례 하나를 정리하고 버전 관리 트레이드오프를 설명해 보세요.',
  '간결한 답변에 집중한 제한 시간 모의 면접을 한 번 진행해 보세요.',
]

const useAppStore = create((set) => ({
  currentUser: null,
  setCurrentUser: (currentUser) => set({ currentUser }),
  interviewMode: '일반',
  interviewQuestions: sampleQuestions,
  currentQuestionIndex: 0,
  interviewAnswers: [],
  feedbackPreview: '',
  interviewResult: defaultResult,
  recentInterviewSessions,
  scoreTrend,
  weaknessTags,
  recommendedNextActions,
  startInterview: (mode) =>
    set({
      interviewMode: mode,
      currentQuestionIndex: 0,
      interviewAnswers: [],
      feedbackPreview: '',
      interviewResult: defaultResult,
    }),
  submitInterviewAnswer: ({ questionId, answer }) =>
    set((state) => {
      const nextAnswers = state.interviewAnswers.filter(
        (item) => item.questionId !== questionId,
      )

      nextAnswers.push({
        questionId,
        answer,
      })

      return {
        interviewAnswers: nextAnswers,
        feedbackPreview:
          answer.trim().length > 0
            ? '좋은 시작입니다. 더 분명한 결과, 구체적인 지표 1개, 고려한 트레이드오프 1개를 추가해 보세요.'
            : '짧게라도 답변을 입력하면 미리보기 피드백을 생성할 수 있습니다.',
      }
    }),
  moveToNextQuestion: () =>
    set((state) => ({
      currentQuestionIndex: Math.min(
        state.currentQuestionIndex + 1,
        state.interviewQuestions.length - 1,
      ),
    })),
  resetInterviewFeedback: () => set({ feedbackPreview: '' }),
}))

export default useAppStore
