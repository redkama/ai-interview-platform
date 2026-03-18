import { useEffect, useMemo, useState } from 'react'
import ResultSummary from '../components/interview/ResultSummary.jsx'
import StatusMessage from '../components/common/StatusMessage.jsx'
import interviewApi from '../api/interviewApi.js'
import usePageTitle from '../hooks/usePageTitle.js'
import useAppStore from '../store/useAppStore.js'

function buildResultModel(report) {
  const feedback = report.feedback ?? {
    relevanceScore: 0,
    logicScore: 0,
    specificityScore: 0,
    overallScore: 0,
    summary: '아직 생성된 종합 피드백이 없습니다.',
  }

  const strengths = []
  const weaknesses = []

  if (feedback.relevanceScore >= 80) {
    strengths.push('질문 의도와 잘 맞는 답변을 했습니다.')
  } else {
    weaknesses.push('질문 의도와의 연결을 더 분명하게 보여줄 필요가 있습니다.')
  }

  if (feedback.logicScore >= 80) {
    strengths.push('답변 구조가 비교적 논리적으로 정리되어 있습니다.')
  } else {
    weaknesses.push('상황, 행동, 결과 순서로 더 구조화하면 좋습니다.')
  }

  if (feedback.specificityScore >= 80) {
    strengths.push('구체적인 근거와 맥락이 충분히 포함되었습니다.')
  } else {
    weaknesses.push('수치, 역할, 결과를 더 구체적으로 넣어 보세요.')
  }

  if (strengths.length === 0) {
    strengths.push('답변을 끝까지 작성하고 제출해 세션 피드백을 계속 누적할 수 있습니다.')
  }

  if (weaknesses.length === 0) {
    weaknesses.push('현재 기준으로 큰 약점은 없지만, 더 간결한 표현을 연습하면 좋습니다.')
  }

  return {
    overallScore: feedback.overallScore,
    strengths,
    weaknesses,
    summaries: (report.questions ?? []).map((item) => ({
      questionId: item.id,
      question: item.questionText,
      answerLength: item.answer?.answerText?.length ?? 0,
      summary: item.answer?.answerText
        ? `답변 길이 ${item.answer.answerText.length}자로 저장되었습니다. 세션 종합 피드백을 참고해 보완해 보세요.`
        : '아직 답변이 저장되지 않았습니다.',
    })),
  }
}

function InterviewResultPage() {
  usePageTitle('면접 결과')
  const currentInterviewSessionId = useAppStore((state) => state.currentInterviewSessionId)
  const [report, setReport] = useState(null)
  const [error, setError] = useState('')

  useEffect(() => {
    if (!currentInterviewSessionId) {
      return
    }

    async function loadReport() {
      try {
        await interviewApi.endSession(currentInterviewSessionId)
        const nextReport = await interviewApi.getResultReport(currentInterviewSessionId)
        setReport(nextReport)
      } catch (requestError) {
        setError(requestError.message ?? '면접 결과를 불러오지 못했습니다.')
      }
    }

    loadReport()
  }, [currentInterviewSessionId])

  const resultModel = useMemo(() => (report ? buildResultModel(report) : null), [report])

  return (
    <section className="workspace-page">
      <div className="workspace-page__hero">
        <p className="page-card__eyebrow">면접 결과</p>
        <h2 className="page-card__title">AI 코칭 결과를 확인해 보세요</h2>
        <p className="page-card__description">
          세션 전체 답변을 기준으로 계산된 점수와 요약 피드백을 결과 화면에서 확인할 수 있습니다.
        </p>
      </div>

      <StatusMessage variant="error" message={error} />

      {resultModel ? <ResultSummary result={resultModel} /> : null}
    </section>
  )
}

export default InterviewResultPage

