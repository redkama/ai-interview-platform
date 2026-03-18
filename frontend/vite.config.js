import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  const backendTarget =
    env.VITE_API_BASE_URL || env.VITE_BACKEND_URL || 'http://localhost:8080'
  const aiServiceTarget =
    env.VITE_AI_SERVICE_BASE_URL || env.VITE_AI_SERVICE_URL || 'http://localhost:8000'

  return {
    plugins: [react()],
    server: {
      proxy: {
        '/api': {
          target: backendTarget,
          changeOrigin: true,
        },
        '/ai': {
          target: aiServiceTarget,
          changeOrigin: true,
        },
      },
    },
  }
})
