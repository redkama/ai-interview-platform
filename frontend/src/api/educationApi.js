import { delay } from './apiUtils.js'
import apiClient from './client.js'

const useStubApi = import.meta.env.VITE_USE_API_STUB === 'true'

const levelQuestions = [
  { id: 'q1', question: '영어로 자기소개를 할 때 어느 정도 편하게 말할 수 있나요?' },
  { id: 'q2', question: '최근 프로젝트를 영어로 설명할 수 있나요?' },
  { id: 'q3', question: '예상하지 못한 추가 질문에도 영어로 이어서 답할 수 있나요?' },
  { id: 'q4', question: '짧은 업무용 이메일을 영어로 작성할 수 있나요?' },
  { id: 'q5', question: '의견이나 트레이드오프를 영어로 설명할 수 있나요?' },
]

const englishLessons = {
  BEGINNER: [
    { id: 1, title: '기본 자기소개 만들기', part: '말하기', level: 'BEGINNER', content: '역할, 강점, 관심사를 짧게 소개하는 연습입니다.', orderNum: 1 },
    { id: 2, title: '면접 기본 표현 익히기', part: '듣기', level: 'BEGINNER', content: '인사, 감사, 짧은 응답 표현을 반복 연습합니다.', orderNum: 2 },
    { id: 3, title: '짧은 경험 답변 쓰기', part: '쓰기', level: 'BEGINNER', content: '한 문장씩 끊어서 경험을 설명하는 패턴을 익힙니다.', orderNum: 3 },
  ],
  INTERMEDIATE: [
    { id: 4, title: '프로젝트 설명 구조화', part: '말하기', level: 'INTERMEDIATE', content: '배경, 문제, 행동, 결과 순서로 경험을 설명합니다.', orderNum: 1 },
    { id: 5, title: 'STAR 답변 패턴', part: '쓰기', level: 'INTERMEDIATE', content: '상황과 행동을 명확하게 나누어 면접 답변을 구성합니다.', orderNum: 2 },
    { id: 6, title: '후속 질문 대응', part: '듣기', level: 'INTERMEDIATE', content: '예상 밖 질문에도 흐름을 잃지 않고 이어 말하는 연습입니다.', orderNum: 3 },
  ],
  ADVANCED: [
    { id: 7, title: '리더십 경험 심화 답변', part: '말하기', level: 'ADVANCED', content: '조율, 설득, 의사결정 과정을 영어로 깊이 있게 설명합니다.', orderNum: 1 },
    { id: 8, title: '트레이드오프 설명 훈련', part: '쓰기', level: 'ADVANCED', content: '기술 선택의 장단점을 논리적으로 정리합니다.', orderNum: 2 },
    { id: 9, title: '경영진 요약 답변', part: '말하기', level: 'ADVANCED', content: '복잡한 프로젝트를 짧고 선명하게 요약하는 연습입니다.', orderNum: 3 },
  ],
}

const historyLessons = {
  ANCIENT: [
    { id: 101, title: '고조선과 초기 국가', era: 'ANCIENT', content: '초기 국가 형성과 건국 신화를 정리합니다.', orderNum: 1 },
    { id: 102, title: '삼한의 성장', era: 'ANCIENT', content: '여러 소국이 성장하는 과정을 살펴봅니다.', orderNum: 2 },
  ],
  THREE_KINGDOMS: [
    { id: 103, title: '삼국의 경쟁과 통일', era: 'THREE_KINGDOMS', content: '고구려, 백제, 신라의 경쟁 구도를 정리합니다.', orderNum: 1 },
    { id: 104, title: '삼국의 문화 교류', era: 'THREE_KINGDOMS', content: '불교와 유학, 기술 전파 흐름을 살펴봅니다.', orderNum: 2 },
  ],
  GORYEO: [
    { id: 105, title: '고려의 정치 체제', era: 'GORYEO', content: '귀족 사회와 중앙 정치 구조를 정리합니다.', orderNum: 1 },
    { id: 106, title: '고려의 대외 관계', era: 'GORYEO', content: '거란, 여진, 몽골과의 관계를 살펴봅니다.', orderNum: 2 },
  ],
  JOSEON: [
    { id: 107, title: '조선의 통치 체제', era: 'JOSEON', content: '성리학 질서와 국가 제도를 정리합니다.', orderNum: 1 },
    { id: 108, title: '조선의 경제와 생활', era: 'JOSEON', content: '농업, 세금, 지역 사회의 변화를 살펴봅니다.', orderNum: 2 },
  ],
  MODERN: [
    { id: 109, title: '개항과 개화', era: 'MODERN', content: '외세 압력과 내부 개혁의 흐름을 정리합니다.', orderNum: 1 },
    { id: 110, title: '근대 국가 수립 시도', era: 'MODERN', content: '개혁안과 정치 변동을 중심으로 살펴봅니다.', orderNum: 2 },
  ],
  CONTEMPORARY: [
    { id: 111, title: '산업화와 민주화', era: 'CONTEMPORARY', content: '현대 한국 사회의 주요 변화를 요약합니다.', orderNum: 1 },
    { id: 112, title: '시민 사회의 성장', era: 'CONTEMPORARY', content: '사회운동과 제도 변화의 연결을 살펴봅니다.', orderNum: 2 },
  ],
}

const historyQuizMap = {
  107: [
    { id: 201, lessonId: 107, question: '조선을 건국한 인물은 누구인가요?', options: ['태조', '세종', '영조', '정조'], answer: '태조', explanation: '조선은 이성계가 태조로 즉위하며 건국했습니다.' },
    { id: 202, lessonId: 107, question: '조선의 기본 법전으로 정비된 것은 무엇인가요?', options: ['훈민정음', '삼국사기', '경국대전', '직지'], answer: '경국대전', explanation: '경국대전은 조선의 통치 체제를 체계화한 핵심 법전입니다.' },
    { id: 203, lessonId: 107, question: '조선 통치 이념의 중심이 된 사상은 무엇인가요?', options: ['불교', '성리학', '도교', '법가'], answer: '성리학', explanation: '조선은 성리학을 국가 운영의 핵심 이념으로 삼았습니다.' },
  ],
  108: [
    { id: 204, lessonId: 108, question: '조선 사회의 주요 경제 기반은 무엇인가요?', options: ['광업', '무역', '농업', '해운'], answer: '농업', explanation: '조선 사회는 농업과 토지세를 중심으로 운영되었습니다.' },
    { id: 205, lessonId: 108, question: '조선의 지방 사회 운영과 가장 관련이 깊은 것은 무엇인가요?', options: ['향약', '팔만대장경', '과전법', '광개토대왕릉비'], answer: '향약', explanation: '향약은 지방 사회의 규범과 자치를 보여 주는 대표 사례입니다.' },
    { id: 206, lessonId: 108, question: '조선 후기 상업 발달과 관련된 장소로 적절한 것은 무엇인가요?', options: ['개성', '한양 시전', '부여', '금관가야'], answer: '한양 시전', explanation: '시전은 조선 후기 상업 활동을 보여 주는 대표 공간입니다.' },
  ],
}

const historyRecords = []

function resolveBaseUrl(path) {
  if (typeof window === 'undefined') {
    return path
  }

  return new URL(path, window.location.origin).toString()
}

function evaluateLevel(answers) {
  const total = answers.reduce((sum, item) => sum + Number(item.score ?? 0), 0)

  if (total >= 16) {
    return { level: 'ADVANCED', score: total, message: '후속 질문과 심화 답변까지 대응할 수 있는 수준입니다.' }
  }
  if (total >= 10) {
    return { level: 'INTERMEDIATE', score: total, message: '경험 설명은 가능하지만 일관성과 유창성을 더 높일 필요가 있습니다.' }
  }
  return { level: 'BEGINNER', score: total, message: '짧은 문장 패턴과 기본 면접 표현부터 시작하는 것이 좋습니다.' }
}

const educationApi = {
  async getLevelTestQuestions() {
    await delay(200)
    return levelQuestions
  },

  async submitLevelTest(answers) {
    await delay(300)
    return evaluateLevel(answers)
  },

  async getEnglishLessons(level = 'BEGINNER') {
    if (useStubApi) {
      await delay(250)
      return englishLessons[level] ?? []
    }

    const response = await apiClient.get(resolveBaseUrl(`/api/education/english/lessons?level=${level}`))
    return response.data?.data ?? []
  },

  async getEnglishProgress() {
    await delay(150)
    return { currentLevel: 'INTERMEDIATE', completedLessons: 7, totalLessons: 12, progressPercent: 58 }
  },

  async getHistoryLessons(era = 'JOSEON') {
    if (useStubApi) {
      await delay(250)
      return historyLessons[era] ?? []
    }

    const response = await apiClient.get(resolveBaseUrl(`/api/education/history/lessons?era=${era}`))
    return response.data?.data ?? []
  },

  async getHistoryQuiz(lessonId) {
    if (useStubApi) {
      await delay(200)
      return historyQuizMap[lessonId] ?? []
    }

    const response = await apiClient.get(resolveBaseUrl(`/api/education/history/lessons/${lessonId}/quiz`))
    return (response.data?.data ?? []).map((item) => ({
      ...item,
      options: Array.isArray(item.options) ? item.options : JSON.parse(item.options ?? '[]'),
    }))
  },

  async getHistoryExplanation(topic, era) {
    if (useStubApi) {
      await delay(220)
      return {
        explanation: `${topic}은(는) ${era} 시대의 핵심 흐름과 연결해서 이해하면 좋습니다. 정치 제도, 사회 변화, 대표 사건을 함께 묶어 보세요.`,
        keyPoints: [
          '시대의 정치적 배경을 먼저 정리합니다.',
          '개혁, 갈등, 생활 변화 중 어떤 맥락인지 연결합니다.',
          '대표 키워드 1개와 결과 1개를 함께 기억합니다.',
        ],
      }
    }

    const response = await apiClient.post(resolveBaseUrl('/api/education/history/explain'), { topic, era })
    return response.data?.data ?? response.data
  },

  async recordHistoryWrongAnswer(entry) {
    await delay(80)
    historyRecords.unshift({ id: `${entry.quizId}-${Date.now()}`, ...entry, createdAt: new Date().toISOString() })
    return historyRecords[0]
  },
}

export default educationApi
