function LoadingBlock({ label = '데이터를 불러오는 중입니다.' }) {
  return (
    <div className="state-block state-block--loading">
      <div className="state-block__spinner" aria-hidden="true" />
      <p>{label}</p>
    </div>
  )
}

export default LoadingBlock
