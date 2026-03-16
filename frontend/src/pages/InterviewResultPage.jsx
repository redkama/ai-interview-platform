import ResultSummary from '../components/interview/ResultSummary.jsx'
import usePageTitle from '../hooks/usePageTitle.js'
import useAppStore from '../store/useAppStore.js'

function InterviewResultPage() {
  usePageTitle('면접 결과')
  const result = useAppStore((state) => state.interviewResult)

  return (
    <section className="workspace-page">
      <div className="workspace-page__hero">
        <p className="page-card__eyebrow">면접 결과</p>
        <h2 className="page-card__title">현재 MVP 결과 요약을 확인하세요.</h2>
        <p className="page-card__description">
          구조를 단순하게 유지해 두어 백엔드 점수 및 피드백 API와 바로 매핑할 수 있습니다.
        </p>
      </div>

      <ResultSummary result={result} />
    </section>
  )
}

export default InterviewResultPage
