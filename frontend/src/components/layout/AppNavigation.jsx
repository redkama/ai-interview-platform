import { NavLink, useNavigate } from 'react-router-dom'
import authApi from '../../api/authApi.js'
import useAppStore from '../../store/useAppStore.js'

const navigationItems = [
  { to: '/login', label: '로그인', requiresAuth: false },
  { to: '/signup', label: '회원가입', requiresAuth: false },
  { to: '/dashboard', label: '대시보드', requiresAuth: true },
  { to: '/resume', label: '이력서', requiresAuth: true },
  { to: '/cover-letter', label: '자기소개서', requiresAuth: true },
  { to: '/job-posting', label: '채용공고', requiresAuth: true },
  { to: '/interview/setup', label: '면접 설정', requiresAuth: true },
  { to: '/interview/session', label: '면접 진행', requiresAuth: true },
  { to: '/interview/result', label: '면접 결과', requiresAuth: true },
]

function AppNavigation() {
  const navigate = useNavigate()
  const currentUser = useAppStore((state) => state.currentUser)
  const clearCurrentUser = useAppStore((state) => state.clearCurrentUser)
  const visibleItems = navigationItems.filter((item) => item.requiresAuth === Boolean(currentUser))

  function handleLogout() {
    authApi.logout()
    clearCurrentUser()
    navigate('/login', { replace: true })
  }

  return (
    <nav className="app-nav" aria-label="기본 내비게이션">
      {visibleItems.map((item) => (
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
      {currentUser ? (
        <button className="button button--secondary" type="button" onClick={handleLogout}>
          로그아웃
        </button>
      ) : null}
    </nav>
  )
}

export default AppNavigation
