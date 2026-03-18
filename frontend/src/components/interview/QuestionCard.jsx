function QuestionCard({ question, index, total, positionTitle }) {
  return (
    <section className="panel interview-card">
      <div className="interview-card__meta">
        <span className="interview-badge">직무: {positionTitle}</span>
        <span className="interview-badge">
          질문 {index + 1} / {total}
        </span>
      </div>
      <div>
        <h3 className="panel__title">현재 질문</h3>
        <p className="interview-card__question">{question.questionText}</p>
      </div>
      <div className="interview-note">
        <strong>안내</strong>
        <p>구체적인 상황, 행동, 결과를 포함해 3~5문장 정도로 답변해 보세요.</p>
      </div>
    </section>
  )
}

export default QuestionCard

