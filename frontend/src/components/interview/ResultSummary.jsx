function ResultSummary({ result }) {
  return (
    <section className="result-layout">
      <section className="panel result-score-card">
        <p className="page-card__eyebrow">종합 점수</p>
        <div className="result-score-card__value">{result.overallScore}</div>
        <p className="panel__subtitle">이후 백엔드에서 받아올 수 있는 MVP 점수 요약입니다.</p>
      </section>

      <section className="panel result-list-card">
        <h3 className="panel__title">강점</h3>
        <ul className="result-list">
          {result.strengths.map((item) => (
            <li key={item}>{item}</li>
          ))}
        </ul>
      </section>

      <section className="panel result-list-card">
        <h3 className="panel__title">보완점</h3>
        <ul className="result-list">
          {result.weaknesses.map((item) => (
            <li key={item}>{item}</li>
          ))}
        </ul>
      </section>

      <section className="panel result-summary-card">
        <h3 className="panel__title">질문별 요약</h3>
        <div className="result-question-list">
          {result.summaries.map((item, index) => (
            <article key={item.questionId} className="result-question-item">
              <div className="result-question-item__header">
                <strong>Q{index + 1}</strong>
                <span>점수 {item.score}</span>
              </div>
              <p className="result-question-item__question">{item.question}</p>
              <p className="panel__subtitle">{item.summary}</p>
            </article>
          ))}
        </div>
      </section>
    </section>
  )
}

export default ResultSummary
