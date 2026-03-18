import { delay } from './apiUtils.js'
import bookApi from './bookApi.js'

const dashboardSnapshot = {
  interviewScoreTrend: [
    { date: '03-05', score: 72 },
    { date: '03-08', score: 78 },
    { date: '03-11', score: 81 },
    { date: '03-14', score: 84 },
    { date: '03-17', score: 88 },
  ],
  learningProgress: {
    english: 58,
    history: 64,
  },
  recommendations: [
    { id: 'rec-1', title: '심화 영어 답변 연습', description: '짧은 문장 답변에서 프로젝트 설명형 답변으로 확장해 보세요.', label: '영어 교육' },
    { id: 'rec-2', title: '조선 개혁 퀴즈 복습', description: '틀린 개념을 다시 보고 제도와 결과를 연결해 보세요.', label: '한국사 교육' },
    { id: 'rec-3', title: '면접 스토리 압축 훈련', description: '하나의 프로젝트 경험을 30초, 60초, 90초 버전으로 말해 보세요.', label: '면접 연습' },
  ],
}

const dashboardApi = {
  async getSummary() {
    await delay(220)
    const orders = await bookApi.getMyOrders()

    return {
      ...dashboardSnapshot,
      recentOrders: orders.slice(0, 3),
    }
  },
}

export default dashboardApi
