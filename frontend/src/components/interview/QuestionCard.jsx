function QuestionCard({ question, index, total, mode }) {
  return (
    <section className="panel interview-card">
      <div className="interview-card__meta">
        <span className="interview-badge">모드: {mode}</span>
        <span className="interview-badge">
          질문 {index + 1} / {total}
        </span>
      </div>
      <div>
        <h3 className="panel__title">현재 질문</h3>
        <p className="interview-card__question">{question.prompt}</p>
      </div>
      <div className="interview-note">
        <strong>의도</strong>
        <p>{question.intent}</p>
      </div>
    </section>
  )
}

export default QuestionCard
