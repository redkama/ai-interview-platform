import { Navigate, Outlet, useLocation } from 'react-router-dom'
import LoadingBlock from '../components/common/LoadingBlock.jsx'
import useAppStore from '../store/useAppStore.js'

function GuardFallback() {
  return (
    <section className="workspace-page">
      <LoadingBlock label="인증 정보를 확인하는 중입니다." />
    </section>
  )
}

export function ProtectedRoute() {
  const location = useLocation()
  const authHydrated = useAppStore((state) => state.authHydrated)
  const currentUser = useAppStore((state) => state.currentUser)

  if (!authHydrated) {
    return <GuardFallback />
  }

  if (!currentUser) {
    return <Navigate to="/login" replace state={{ from: location }} />
  }

  return <Outlet />
}

export function PublicOnlyRoute() {
  const authHydrated = useAppStore((state) => state.authHydrated)
  const currentUser = useAppStore((state) => state.currentUser)

  if (!authHydrated) {
    return <GuardFallback />
  }

  if (currentUser) {
    return <Navigate to="/dashboard" replace />
  }

  return <Outlet />
}

export function IndexRedirect() {
  const authHydrated = useAppStore((state) => state.authHydrated)
  const currentUser = useAppStore((state) => state.currentUser)

  if (!authHydrated) {
    return <GuardFallback />
  }

  return <Navigate to={currentUser ? '/dashboard' : '/login'} replace />
}
