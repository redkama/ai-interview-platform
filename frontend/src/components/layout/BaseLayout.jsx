import { Outlet } from 'react-router-dom'
import AppNavigation from './AppNavigation.jsx'

function BaseLayout() {
  return (
    <div className="app-shell">
      <header className="app-header">
        <div>
          <p className="app-header__eyebrow">AI 멘토 플랫폼</p>
          <h1 className="app-header__title">준비부터 모의 면접까지 한 흐름으로 연결합니다</h1>
          <p className="app-header__description">
            이 프로젝트는 로그인, 문서 관리, 면접 연습, 교육, 도서 구매 흐름을
            포트폴리오와 시연에 맞게 한 화면 구조로 정리한 MVP입니다.
          </p>
        </div>
        <AppNavigation />
      </header>

      <main className="app-main">
        <Outlet />
      </main>
    </div>
  )
}

export default BaseLayout
