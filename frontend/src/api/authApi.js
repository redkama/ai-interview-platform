import { extractPayload } from './apiUtils.js'
import apiClient, {
  clearAuthSession,
  getStoredAuthSession,
  persistAuthSession,
} from './client.js'
import { stubLogin, stubSignup } from './stubDatabase.js'

const useStubApi = import.meta.env.VITE_USE_API_STUB === 'true'

function normalizeAuthPayload(payload) {
  return {
    user: {
      id: payload.user?.id ?? payload.userId,
      name: payload.user?.name ?? payload.name ?? payload.email,
      email: payload.user?.email ?? payload.email,
      role: payload.user?.role ?? payload.role,
    },
    accessToken: payload.accessToken,
    accessTokenExpiresAt: payload.accessTokenExpiresAt ?? null,
    refreshToken: payload.refreshToken,
    refreshTokenExpiresAt: payload.refreshTokenExpiresAt ?? null,
  }
}

const authApi = {
  async login(payload) {
    if (useStubApi) {
      const result = await stubLogin(payload)
      persistAuthSession(result)
      return result
    }

    const response = await apiClient.post('/auth/login', payload)
    const result = normalizeAuthPayload(extractPayload(response))
    persistAuthSession(result)
    return result
  },
  async signup(payload) {
    if (useStubApi) {
      const result = await stubSignup(payload)
      persistAuthSession(result)
      return result
    }

    const response = await apiClient.post('/auth/signup', {
      name: payload.name,
      email: payload.email,
      password: payload.password,
    })
    const result = normalizeAuthPayload(extractPayload(response))
    persistAuthSession(result)
    return result
  },
  async logout() {
    const session = getStoredAuthSession()

    if (!useStubApi && session?.refreshToken) {
      try {
        await apiClient.post('/auth/logout', {
          refreshToken: session.refreshToken,
        })
      } catch {
      }
    }

    clearAuthSession()
  },
}

export default authApi

