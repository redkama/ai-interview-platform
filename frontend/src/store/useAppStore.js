import { create } from 'zustand'

const INTERVIEW_SESSION_KEY = 'aimentor.currentInterviewSessionId'

function readStoredInterviewSessionId() {
  if (typeof window === 'undefined') {
    return null
  }

  return window.sessionStorage.getItem(INTERVIEW_SESSION_KEY)
}

function persistInterviewSessionId(sessionId) {
  if (typeof window === 'undefined') {
    return
  }

  if (sessionId) {
    window.sessionStorage.setItem(INTERVIEW_SESSION_KEY, String(sessionId))
    return
  }

  window.sessionStorage.removeItem(INTERVIEW_SESSION_KEY)
}

const recentInterviewSessions = [
  {
    id: 'session-2026-03-13',
    date: '2026-03-13',
    mode: '기술',
    score: 84,
    summary: '장애 대응과 주도성 설명이 강점으로 보였습니다.',
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
    summary: '기본 답변은 좋았지만 구조를 더 다듬을 필요가 있습니다.',
  },
]

const scoreTrend = recentInterviewSessions
  .slice()
  .reverse()
  .map((session) => ({
    date: session.date,
    score: session.score,
  }))

const weaknessTags = ['답변 구조', '지표 명확화', '트레이드오프 설명', 'API 버전 관리 설명']

const recommendedNextActions = [
  '정량적 결과가 포함된 STAR 답변을 두 번 더 연습해 보세요.',
  'API 설계 예시 하나를 정리하고 버전 관리와 트레이드오프를 설명해 보세요.',
  '간결한 답변에 집중하는 제한 시간 모의 면접을 한 번 진행해 보세요.',
]

const useAppStore = create((set) => ({
  currentUser: null,
  authHydrated: false,
  currentInterviewSessionId: readStoredInterviewSessionId(),
  recentInterviewSessions,
  scoreTrend,
  weaknessTags,
  recommendedNextActions,
  setCurrentUser: (currentUser) =>
    set({
      currentUser,
      authHydrated: true,
    }),
  hydrateAuth: (session) =>
    set({
      currentUser: session?.user ?? null,
      authHydrated: true,
      currentInterviewSessionId: readStoredInterviewSessionId(),
    }),
  clearCurrentUser: () => {
    persistInterviewSessionId(null)
    set({
      currentUser: null,
      authHydrated: true,
      currentInterviewSessionId: null,
    })
  },
  setCurrentInterviewSessionId: (currentInterviewSessionId) => {
    persistInterviewSessionId(currentInterviewSessionId)
    set({
      currentInterviewSessionId,
    })
  },
  clearCurrentInterviewSessionId: () => {
    persistInterviewSessionId(null)
    set({
      currentInterviewSessionId: null,
    })
  },
}))

export default useAppStore