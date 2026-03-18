import TextAreaField from '../forms/TextAreaField.jsx'

function AnswerInput({
  answer,
  onChange,
  onSubmit,
  onRecordPlaceholder,
  isSubmitting,
}) {
  return (
    <section className="panel">
      <div className="panel__header">
        <div>
          <h3 className="panel__title">답변</h3>
          <p className="panel__subtitle">
            현재는 텍스트 입력으로 진행하고, 이후 음성 기능으로 확장할 수 있습니다.
          </p>
        </div>
      </div>

      <TextAreaField
        label="답변 초안"
        rows={8}
        value={answer}
        onChange={(event) => onChange(event.target.value)}
        placeholder="상황, 행동, 결과 순서로 답변을 정리해 보세요."
      />

      <div className="button-row">
        <button className="button button--secondary" type="button" onClick={onRecordPlaceholder}>
          음성 녹음 자리표시자
        </button>
        <button className="button" type="button" onClick={onSubmit} disabled={isSubmitting}>
          {isSubmitting ? '제출 중...' : '답변 제출'}
        </button>
      </div>
    </section>
  )
}

export default AnswerInput
