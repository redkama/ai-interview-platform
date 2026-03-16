import { NavLink } from 'react-router-dom'

const navigationItems = [
  { to: '/login', label: '로그인' },
  { to: '/signup', label: '회원가입' },
  { to: '/dashboard', label: '대시보드' },
  { to: '/resume', label: '이력서' },
  { to: '/cover-letter', label: '자기소개서' },
  { to: '/job-posting', label: '채용공고' },
  { to: '/interview/setup', label: '면접 설정' },
  { to: '/interview/session', label: '면접 진행' },
  { to: '/interview/result', label: '면접 결과' },
]

function AppNavigation() {
  return (
    <nav className="app-nav" aria-label="기본 내비게이션">
      {navigationItems.map((item) => (
        <NavLink
          key={item.to}
          to={item.to}
          className={({ isActive }) =>
            isActive ? 'app-nav__link app-nav__link--active' : 'app-nav__link'
          }
        >
          {item.label}
        </NavLink>
      ))}
    </nav>
  )
}

export default AppNavigation
