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
            현재 프론트엔드는 로그인과 문서 CRUD 화면을 중심으로 구성되어 있으며,
            면접 데모에 바로 연결하기 쉬운 레이아웃을 제공합니다.
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
