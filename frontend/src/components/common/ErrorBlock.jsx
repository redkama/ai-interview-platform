function ErrorBlock({ message, onRetry }) {
  return (
    <div className="state-block state-block--error">
      <p>{message}</p>
      {onRetry ? (
        <button className="button button--secondary" type="button" onClick={onRetry}>
          다시 시도
        </button>
      ) : null}
    </div>
  )
}

export default ErrorBlock

