import { useEffect, useMemo, useState } from 'react'
import educationApi from '../api/educationApi.js'
import usePageTitle from '../hooks/usePageTitle.js'

const eraTabs = ['ANCIENT', 'THREE_KINGDOMS', 'GORYEO', 'JOSEON', 'MODERN', 'CONTEMPORARY']

const eraLabelMap = {
  ANCIENT: '고대',
  THREE_KINGDOMS: '삼국',
  GORYEO: '고려',
  JOSEON: '조선',
  MODERN: '근대',
  CONTEMPORARY: '현대',
}

function KoreanHistoryPage() {
  usePageTitle('한국사 교육')
  const [selectedEra, setSelectedEra] = useState('JOSEON')
  const [lessons, setLessons] = useState([])
  const [selectedLesson, setSelectedLesson] = useState(null)
  const [quizItems, setQuizItems] = useState([])
  const [feedbackMap, setFeedbackMap] = useState({})
  const [wrongAnswers, setWrongAnswers] = useState([])
  const [explanation, setExplanation] = useState(null)

  useEffect(() => {
    educationApi.getHistoryLessons(selectedEra).then((items) => {
      setLessons(items)
      setSelectedLesson(items[0] ?? null)
    })
  }, [selectedEra])

  useEffect(() => {
    if (!selectedLesson) {
      setQuizItems([])
      setExplanation(null)
      return
    }

    educationApi.getHistoryQuiz(selectedLesson.id).then(setQuizItems)
    educationApi.getHistoryExplanation(selectedLesson.title, selectedLesson.era).then(setExplanation)
  }, [selectedLesson])

  const wrongAnswerCount = useMemo(
    () => wrongAnswers.filter((item) => item.lessonId === selectedLesson?.id).length,
    [selectedLesson?.id, wrongAnswers],
  )

  async function handleChoice(quiz, selectedAnswer) {
    const isCorrect = selectedAnswer === quiz.answer
    setFeedbackMap((current) => ({
      ...current,
      [quiz.id]: {
        selectedAnswer,
        isCorrect,
        explanation: quiz.explanation,
      },
    }))

    if (!isCorrect) {
      const note = {
        quizId: quiz.id,
        lessonId: quiz.lessonId,
        question: quiz.question,
        selectedAnswer,
        correctAnswer: quiz.answer,
        explanation: quiz.explanation,
      }
      setWrongAnswers((current) => [note, ...current.filter((item) => item.quizId !== quiz.id)])
      await educationApi.recordHistoryWrongAnswer(note)
    }
  }

  return (
    <section className="workspace-page">
      <div className="workspace-page__hero">
        <p className="page-card__eyebrow">한국사 교육</p>
        <h2 className="page-card__title">시대별 레슨과 퀴즈</h2>
        <p className="page-card__description">
          시대를 바꿔가며 레슨을 보고, 4지선다 퀴즈를 풀고, 오답 노트를 쌓을 수 있습니다.
        </p>
      </div>

      <section className="panel">
        <div className="tab-row" role="tablist" aria-label="한국사 시대 필터">
          {eraTabs.map((era) => (
            <button
              key={era}
              className={selectedEra === era ? 'tab-chip tab-chip--active' : 'tab-chip'}
              type="button"
              onClick={() => setSelectedEra(era)}
            >
              {eraLabelMap[era]}
            </button>
          ))}
        </div>

        <div className="lesson-grid">
          {lessons.map((lesson) => (
            <button
              key={lesson.id}
              className={selectedLesson?.id === lesson.id ? 'lesson-card lesson-card--active' : 'lesson-card'}
              type="button"
              onClick={() => setSelectedLesson(lesson)}
            >
              <p className="lesson-card__meta">{eraLabelMap[lesson.era]}</p>
              <h3 className="panel__title">{lesson.title}</h3>
              <p className="panel__subtitle">{lesson.content}</p>
            </button>
          ))}
        </div>
      </section>

      {selectedLesson ? (
        <div className="history-grid">
          <section className="panel">
            <div className="panel__header">
              <div>
                <h3 className="panel__title">퀴즈</h3>
                <p className="panel__subtitle">{selectedLesson.title}</p>
              </div>
              <span className="dashboard-score-chip">오답 {wrongAnswerCount}개</span>
            </div>

            <div className="quiz-stack">
              {quizItems.map((quiz) => {
                const feedback = feedbackMap[quiz.id]
                return (
                  <article key={quiz.id} className="quiz-card">
                    <h4>{quiz.question}</h4>
                    <div className="quiz-options">
                      {quiz.options.map((option) => (
                        <button
                          key={option}
                          className={feedback?.selectedAnswer === option ? 'quiz-option quiz-option--selected' : 'quiz-option'}
                          type="button"
                          onClick={() => handleChoice(quiz, option)}
                        >
                          {option}
                        </button>
                      ))}
                    </div>

                    {feedback ? (
                      <div className={feedback.isCorrect ? 'status-message status-message--success' : 'status-message status-message--error'}>
                        {feedback.isCorrect ? '정답입니다.' : `오답입니다. 정답: ${quiz.answer}`}
                        <div className="quiz-feedback-copy">{feedback.explanation}</div>
                      </div>
                    ) : null}
                  </article>
                )
              })}
            </div>
          </section>

          <section className="history-side-column">
            <article className="panel">
              <div className="panel__header">
                <div>
                  <h3 className="panel__title">AI 해설</h3>
                  <p className="panel__subtitle">튜터링 지원용 자리표시자 응답입니다.</p>
                </div>
              </div>
              <p className="panel__subtitle history-ai-copy">{explanation?.explanation}</p>
              <ul className="page-card__list">
                {(explanation?.keyPoints ?? []).map((item) => (
                  <li key={item}>{item}</li>
                ))}
              </ul>
            </article>

            <article className="panel">
              <div className="panel__header">
                <div>
                  <h3 className="panel__title">오답 노트</h3>
                  <p className="panel__subtitle">헷갈린 개념을 짧게 다시 볼 수 있도록 모아둡니다.</p>
                </div>
              </div>

              <div className="dashboard-action-list">
                {wrongAnswers.length === 0 ? (
                  <div className="empty-card">
                    <h4>아직 오답이 없습니다</h4>
                    <p>틀린 퀴즈가 생기면 이곳에 자동으로 모입니다.</p>
                  </div>
                ) : (
                  wrongAnswers.map((item) => (
                    <article key={item.quizId} className="dashboard-action-item">
                      <strong>{item.question}</strong>
                      <p>내 답: {item.selectedAnswer}</p>
                      <p>정답: {item.correctAnswer}</p>
                    </article>
                  ))
                )}
              </div>
            </article>
          </section>
        </div>
      ) : null}
    </section>
  )
}

export default KoreanHistoryPage
