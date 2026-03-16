function FeedbackCard({ feedback }) {
  return (
    <section className="panel">
      <div className="panel__header">
        <div>
          <h3 className="panel__title">피드백 미리보기</h3>
          <p className="panel__subtitle">백엔드 평가 기능이 연결되기 전까지 임시 로컬 미리보기를 사용합니다.</p>
        </div>
      </div>

      <div className="feedback-card">
        <p>{feedback || '답변을 제출하면 미리보기 피드백이 표시됩니다.'}</p>
      </div>
    </section>
  )
}

export default FeedbackCard
