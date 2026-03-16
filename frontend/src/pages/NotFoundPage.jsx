import { Link } from 'react-router-dom'
import usePageTitle from '../hooks/usePageTitle.js'

function NotFoundPage() {
  usePageTitle('페이지 없음')

  return (
    <section className="page-card page-card--compact">
      <div className="page-card__content">
        <p className="page-card__eyebrow">404</p>
        <h2 className="page-card__title">페이지를 찾을 수 없습니다</h2>
        <p className="page-card__description">
          요청한 경로가 현재 프론트엔드 구성에 존재하지 않습니다.
        </p>
        <Link className="page-card__action" to="/dashboard">
          대시보드로 이동
        </Link>
      </div>
    </section>
  )
}

export default NotFoundPage
