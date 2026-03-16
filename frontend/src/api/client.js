import axios from 'axios'
import useAppStore from '../store/useAppStore.js'

const AUTH_STORAGE_KEY = 'aimentor.auth'
const AUTH_NOTICE_KEY = 'aimentor.auth.notice'
const API_BASE_URL = '/api/v1'

function readStoredAuth() {
  if (typeof window === 'undefined') {
    return null
  }

  const storedValue = window.localStorage.getItem(AUTH_STORAGE_KEY)

  if (!storedValue) {
    return null
  }

  try {
    return JSON.parse(storedValue)
  } catch {
    window.localStorage.removeItem(AUTH_STORAGE_KEY)
    return null
  }
}

function extractPayload(response) {
  return response?.data?.data ?? response?.data ?? null
}

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

function syncAuthState(session) {
  useAppStore.getState().hydrateAuth(session)
}

export function setAuthNotice(message) {
  if (typeof window === 'undefined') {
    return
  }

  window.sessionStorage.setItem(AUTH_NOTICE_KEY, message)
}

export function consumeAuthNotice() {
  if (typeof window === 'undefined') {
    return ''
  }

  const message = window.sessionStorage.getItem(AUTH_NOTICE_KEY) ?? ''
  window.sessionStorage.removeItem(AUTH_NOTICE_KEY)
  return message
}

function handleSessionExpired() {
  clearAuthSession()
  useAppStore.getState().clearCurrentUser()
  setAuthNotice('세션이 만료되었거나 인증이 유효하지 않습니다. 다시 로그인해 주세요.')

  if (typeof window !== 'undefined' && window.location.pathname !== '/login') {
    window.location.replace('/login')
  }
}

export function getStoredAuthSession() {
  return readStoredAuth()
}

export function persistAuthSession(session) {
  if (typeof window === 'undefined') {
    return
  }

  window.localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(session))
  syncAuthState(session)
}

export function clearAuthSession() {
  if (typeof window === 'undefined') {
    return
  }

  window.localStorage.removeItem(AUTH_STORAGE_KEY)
}

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

const authClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

let refreshRequestPromise = null

async function refreshAccessToken() {
  const session = readStoredAuth()

  if (!session?.refreshToken) {
    throw new Error('리프레시 토큰이 없습니다.')
  }

  const response = await authClient.post('/auth/refresh', {
    refreshToken: session.refreshToken,
  })
  const nextSession = normalizeAuthPayload(extractPayload(response))
  persistAuthSession(nextSession)
  return nextSession
}

apiClient.interceptors.request.use((config) => {
  const session = readStoredAuth()

  if (session?.accessToken) {
    config.headers.Authorization = `Bearer ${session.accessToken}`
  }

  return config
})

apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config
    const status = error.response?.status
    const isAuthRequest = originalRequest?.url?.includes('/auth/')

    if (status !== 401 || !originalRequest || originalRequest._retry || isAuthRequest) {
      return Promise.reject(error)
    }

    try {
      originalRequest._retry = true

      if (!refreshRequestPromise) {
        refreshRequestPromise = refreshAccessToken().finally(() => {
          refreshRequestPromise = null
        })
      }

      const refreshedSession = await refreshRequestPromise
      originalRequest.headers = originalRequest.headers ?? {}
      originalRequest.headers.Authorization = `Bearer ${refreshedSession.accessToken}`
      return apiClient(originalRequest)
    } catch (refreshError) {
      handleSessionExpired()
      return Promise.reject(refreshError)
    }
  },
)

export default apiClient
