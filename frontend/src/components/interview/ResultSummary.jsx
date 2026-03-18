function ResultSummary({ result }) {
  return (
    <section className="result-layout">
      <section className="panel result-score-card">
        <p className="page-card__eyebrow">종합 점수</p>
        <div className="result-score-card__value">{result.overallScore}</div>
        <p className="panel__subtitle">세션 전체 답변을 기준으로 계산된 AI 코칭 결과입니다.</p>
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
        <h3 className="panel__title">질문별 답변 요약</h3>
        <div className="result-question-list">
          {result.summaries.map((item, index) => (
            <article key={item.questionId} className="result-question-item">
              <div className="result-question-item__header">
                <strong>Q{index + 1}</strong>
                <span>{item.answerLength}자 답변</span>
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

