import { useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import AnswerInput from '../components/interview/AnswerInput.jsx'
import FeedbackCard from '../components/interview/FeedbackCard.jsx'
import QuestionCard from '../components/interview/QuestionCard.jsx'
import StatusMessage from '../components/common/StatusMessage.jsx'
import usePageTitle from '../hooks/usePageTitle.js'
import useAppStore from '../store/useAppStore.js'

function InterviewSessionPage() {
  usePageTitle('면접 진행')
  const navigate = useNavigate()
  const interviewMode = useAppStore((state) => state.interviewMode)
  const questions = useAppStore((state) => state.interviewQuestions)
  const currentQuestionIndex = useAppStore((state) => state.currentQuestionIndex)
  const interviewAnswers = useAppStore((state) => state.interviewAnswers)
  const feedbackPreview = useAppStore((state) => state.feedbackPreview)
  const submitInterviewAnswer = useAppStore((state) => state.submitInterviewAnswer)
  const moveToNextQuestion = useAppStore((state) => state.moveToNextQuestion)
  const resetInterviewFeedback = useAppStore((state) => state.resetInterviewFeedback)
  const currentQuestion = questions[currentQuestionIndex]
  const savedAnswer = useMemo(
    () =>
      interviewAnswers.find((item) => item.questionId === currentQuestion?.id)?.answer ?? '',
    [currentQuestion?.id, interviewAnswers],
  )
  const [answer, setAnswer] = useState(savedAnswer)
  const [statusMessage, setStatusMessage] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)

  useEffect(() => {
    setAnswer(savedAnswer)
  }, [savedAnswer])

  if (!currentQuestion) {
    return (
      <section className="workspace-page">
        <div className="workspace-page__hero">
          <p className="page-card__eyebrow">면접 진행</p>
          <h2 className="page-card__title">활성화된 세션이 없습니다.</h2>
          <p className="page-card__description">
            면접 진행 화면을 열기 전에 설정 페이지에서 세션을 시작하세요.
          </p>
        </div>

        <div className="button-row">
          <button className="button" type="button" onClick={() => navigate('/interview/setup')}>
            설정으로 이동
          </button>
        </div>
      </section>
    )
  }

  async function handleSubmitAnswer() {
    setIsSubmitting(true)
    setStatusMessage('')

    submitInterviewAnswer({
      questionId: currentQuestion.id,
      answer,
    })

    setStatusMessage('답변이 제출되었고 미리보기 피드백이 갱신되었습니다.')
    setIsSubmitting(false)
  }

  function handleVoicePlaceholder() {
    setStatusMessage('음성 녹음 연동 기능이 이 위치에 연결될 예정입니다.')
  }

  function handleNextQuestion() {
    const isLastQuestion = currentQuestionIndex >= questions.length - 1

    if (isLastQuestion) {
      navigate('/interview/result')
      return
    }

    moveToNextQuestion()
    resetInterviewFeedback()
    const nextAnswer =
      interviewAnswers.find((item) => item.questionId === questions[currentQuestionIndex + 1]?.id)
        ?.answer ?? ''
    setAnswer(nextAnswer)
    setStatusMessage('')
  }

  return (
    <section className="workspace-page">
      <div className="workspace-page__hero">
        <p className="page-card__eyebrow">면접 진행</p>
        <h2 className="page-card__title">질문, 답변, 피드백 흐름을 간단하게 진행합니다.</h2>
        <p className="page-card__description">
          현재는 로컬 상태를 사용해 두어, 이후 백엔드 평가 API를 붙여도 UI 구조를 크게 바꾸지 않도록 했습니다.
        </p>
      </div>

      <StatusMessage variant="success" message={statusMessage} />

      <div className="workspace-page">
        <QuestionCard
          question={currentQuestion}
          index={currentQuestionIndex}
          total={questions.length}
          mode={interviewMode}
        />

        <AnswerInput
          answer={answer}
          onChange={setAnswer}
          onSubmit={handleSubmitAnswer}
          onRecordPlaceholder={handleVoicePlaceholder}
          isSubmitting={isSubmitting}
        />

        <FeedbackCard feedback={feedbackPreview} />

        <div className="button-row">
          <button className="button" type="button" onClick={handleNextQuestion}>
            {currentQuestionIndex >= questions.length - 1 ? '결과 보기' : '다음 질문'}
          </button>
        </div>
      </div>
    </section>
  )
}

export default InterviewSessionPage
