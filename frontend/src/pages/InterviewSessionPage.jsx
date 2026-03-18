import { useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import AnswerInput from '../components/interview/AnswerInput.jsx'
import FeedbackCard from '../components/interview/FeedbackCard.jsx'
import QuestionCard from '../components/interview/QuestionCard.jsx'
import StatusMessage from '../components/common/StatusMessage.jsx'
import interviewApi from '../api/interviewApi.js'
import usePageTitle from '../hooks/usePageTitle.js'
import useAppStore from '../store/useAppStore.js'

function InterviewSessionPage() {
  usePageTitle('면접 진행')
  const navigate = useNavigate()
  const currentInterviewSessionId = useAppStore((state) => state.currentInterviewSessionId)
  const [session, setSession] = useState(null)
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0)
  const [answer, setAnswer] = useState('')
  const [statusMessage, setStatusMessage] = useState('')
  const [error, setError] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)

  useEffect(() => {
    if (!currentInterviewSessionId) {
      return
    }

    async function loadSession() {
      try {
        const nextSession = await interviewApi.getSessionDetail(currentInterviewSessionId)
        setSession(nextSession)
      } catch (requestError) {
        setError(requestError.message ?? '면접 세션을 불러오지 못했습니다.')
      }
    }

    loadSession()
  }, [currentInterviewSessionId])

  const currentQuestion = useMemo(
    () => session?.questions?.[currentQuestionIndex] ?? null,
    [currentQuestionIndex, session?.questions],
  )

  useEffect(() => {
    setAnswer(currentQuestion?.answer?.answerText ?? '')
  }, [currentQuestion?.answer?.answerText, currentQuestion?.id])

  if (!currentInterviewSessionId) {
    return (
      <section className="workspace-page">
        <div className="workspace-page__hero">
          <p className="page-card__eyebrow">면접 진행</p>
          <h2 className="page-card__title">진행 중인 면접 세션이 없습니다.</h2>
          <p className="page-card__description">면접 설정 화면에서 세션을 먼저 시작해 주세요.</p>
        </div>

        <div className="button-row">
          <button className="button" type="button" onClick={() => navigate('/interview/setup')}>
            설정으로 이동
          </button>
        </div>
      </section>
    )
  }

  async function refreshSession() {
    const nextSession = await interviewApi.getSessionDetail(currentInterviewSessionId)
    setSession(nextSession)
    return nextSession
  }

  async function handleSubmitAnswer() {
    if (!currentQuestion) {
      return
    }

    setIsSubmitting(true)
    setStatusMessage('')
    setError('')

    try {
      await interviewApi.saveAnswer(currentInterviewSessionId, {
        questionId: currentQuestion.id,
        answerText: answer,
        audioUrl: null,
      })
      await refreshSession()
      setStatusMessage('답변이 저장되었고 AI 코칭 요약이 갱신되었습니다.')
    } catch (requestError) {
      setError(requestError.message ?? '답변 저장에 실패했습니다.')
    } finally {
      setIsSubmitting(false)
    }
  }

  function handleVoicePlaceholder() {
    setStatusMessage('음성 입력 연동은 아직 준비 중입니다. 현재는 텍스트 답변으로 진행해 주세요.')
  }

  function handleNextQuestion() {
    if (!session?.questions?.length) {
      return
    }

    const isLastQuestion = currentQuestionIndex >= session.questions.length - 1
    if (isLastQuestion) {
      navigate('/interview/result')
      return
    }

    setCurrentQuestionIndex((current) => current + 1)
    setStatusMessage('')
    setError('')
  }

  return (
    <section className="workspace-page">
      <div className="workspace-page__hero">
        <p className="page-card__eyebrow">면접 진행</p>
        <h2 className="page-card__title">AI가 생성한 질문으로 실제 면접 코칭을 진행합니다</h2>
        <p className="page-card__description">
          질문은 백엔드가 AI 서비스에 요청해 생성했고, 답변을 저장할 때마다 AI 피드백이 다시 계산됩니다.
        </p>
      </div>

      <StatusMessage variant="error" message={error} />
      <StatusMessage variant="success" message={statusMessage} />

      {currentQuestion && session ? (
        <div className="workspace-page">
          <QuestionCard
            question={currentQuestion}
            index={currentQuestionIndex}
            total={session.questions.length}
            positionTitle={session.positionTitle}
          />

          <AnswerInput
            answer={answer}
            onChange={setAnswer}
            onSubmit={handleSubmitAnswer}
            onRecordPlaceholder={handleVoicePlaceholder}
            isSubmitting={isSubmitting}
          />

          <FeedbackCard feedback={session.feedback} />

          <div className="button-row">
            <button className="button button--secondary" type="button" onClick={() => navigate('/interview/setup')}>
              새 세션 만들기
            </button>
            <button className="button" type="button" onClick={handleNextQuestion}>
              {currentQuestionIndex >= session.questions.length - 1 ? '결과 보기' : '다음 질문'}
            </button>
          </div>
        </div>
      ) : null}
    </section>
  )
}

export default InterviewSessionPage

