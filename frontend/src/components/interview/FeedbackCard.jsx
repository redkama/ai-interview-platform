function FeedbackCard({ feedback }) {
  if (!feedback) {
    return (
      <section className="panel">
        <div className="panel__header">
          <div>
            <h3 className="panel__title">AI 코칭 요약</h3>
            <p className="panel__subtitle">답변을 저장하면 AI가 점수와 요약 피드백을 생성합니다.</p>
          </div>
        </div>

        <div className="feedback-card">
          <p>아직 생성된 피드백이 없습니다.</p>
        </div>
      </section>
    )
  }

  return (
    <section className="panel">
      <div className="panel__header">
        <div>
          <h3 className="panel__title">AI 코칭 요약</h3>
          <p className="panel__subtitle">현재까지 저장된 답변 기준의 세션 종합 피드백입니다.</p>
        </div>
      </div>

      <div className="feedback-card">
        <div className="progress-stack">
          <div className="progress-stack__row">
            <strong>관련성</strong>
            <span>{feedback.relevanceScore}점</span>
          </div>
          <div className="progress-stack__row">
            <strong>논리성</strong>
            <span>{feedback.logicScore}점</span>
          </div>
          <div className="progress-stack__row">
            <strong>구체성</strong>
            <span>{feedback.specificityScore}점</span>
          </div>
          <div className="progress-stack__row">
            <strong>종합</strong>
            <span>{feedback.overallScore}점</span>
          </div>
        </div>
        <p>{feedback.summary}</p>
      </div>
    </section>
  )
}

export default FeedbackCard

