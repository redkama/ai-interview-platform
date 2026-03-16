import { useEffect } from 'react'
import { getStoredAuthSession } from '../api/client.js'
import useAppStore from '../store/useAppStore.js'

function AuthBootstrap({ children }) {
  const authHydrated = useAppStore((state) => state.authHydrated)
  const hydrateAuth = useAppStore((state) => state.hydrateAuth)

  useEffect(() => {
    if (authHydrated) {
      return
    }

    hydrateAuth(getStoredAuthSession())
  }, [authHydrated, hydrateAuth])

  return children
}

export default AuthBootstrap
