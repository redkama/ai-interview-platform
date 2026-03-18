import { useEffect, useMemo, useState } from 'react'
import educationApi from '../api/educationApi.js'
import usePageTitle from '../hooks/usePageTitle.js'

const scoreOptions = [
  { value: 1, label: '도움이 많이 필요해요' },
  { value: 2, label: '조금은 답할 수 있어요' },
  { value: 3, label: '준비하면 답할 수 있어요' },
  { value: 4, label: '자신 있게 답할 수 있어요' },
]

function LevelTestPage() {
  usePageTitle('영어 레벨 테스트')
  const [questions, setQuestions] = useState([])
  const [answers, setAnswers] = useState({})
  const [result, setResult] = useState(null)
  const [isSubmitting, setIsSubmitting] = useState(false)

  useEffect(() => {
    educationApi.getLevelTestQuestions().then(setQuestions)
  }, [])

  const isComplete = useMemo(
    () => questions.length > 0 && questions.every((item) => answers[item.id]),
    [answers, questions],
  )

  async function handleSubmit(event) {
    event.preventDefault()
    if (!isComplete) {
      return
    }

    setIsSubmitting(true)
    try {
      const payload = questions.map((item) => ({
        id: item.id,
        score: answers[item.id],
      }))
      const nextResult = await educationApi.submitLevelTest(payload)
      setResult(nextResult)
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <section className="workspace-page">
      <div className="workspace-page__hero">
        <p className="page-card__eyebrow">영어 교육</p>
        <h2 className="page-card__title">레벨 테스트</h2>
        <p className="page-card__description">
          다섯 개의 짧은 자기진단 문항에 답하고 추천 시작 레벨을 확인해 보세요.
        </p>
      </div>

      <form className="panel education-test-form" onSubmit={handleSubmit}>
        {questions.map((item, index) => (
          <article key={item.id} className="education-question-card">
            <div>
              <p className="education-question-card__index">문항 {index + 1}</p>
              <h3 className="panel__title">{item.question}</h3>
            </div>

            <div className="choice-row">
              {scoreOptions.map((option) => (
                <label key={option.value} className="choice-chip">
                  <input
                    type="radio"
                    name={item.id}
                    value={option.value}
                    checked={Number(answers[item.id]) === option.value}
                    onChange={() =>
                      setAnswers((current) => ({
                        ...current,
                        [item.id]: option.value,
                      }))
                    }
                  />
                  <span>{option.label}</span>
                </label>
              ))}
            </div>
          </article>
        ))}

        <div className="button-row">
          <button className="button" type="submit" disabled={!isComplete || isSubmitting}>
            {isSubmitting ? '제출 중...' : '레벨 테스트 제출'}
          </button>
        </div>
      </form>

      {result ? (
        <section className="panel result-highlight-card">
          <div className="result-highlight-card__badge">{result.level}</div>
          <div>
            <h3 className="panel__title">추천 레벨 결과</h3>
            <p className="panel__subtitle">점수 {result.score} / 20</p>
            <p className="education-result-copy">{result.message}</p>
          </div>
        </section>
      ) : null}
    </section>
  )
}

export default LevelTestPage
