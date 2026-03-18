import { extractPayload } from './apiUtils.js'
import apiClient from './client.js'

const interviewApi = {
  async startSession(payload) {
    const response = await apiClient.post('/interviews/sessions', payload)
    return extractPayload(response)
  },

  async getSessionDetail(sessionId) {
    const response = await apiClient.get(`/interviews/sessions/${sessionId}`)
    return extractPayload(response)
  },

  async saveAnswer(sessionId, payload) {
    const response = await apiClient.post(`/interviews/sessions/${sessionId}/answers`, payload)
    return extractPayload(response)
  },

  async endSession(sessionId) {
    const response = await apiClient.post(`/interviews/sessions/${sessionId}/end`)
    return extractPayload(response)
  },

  async getResultReport(sessionId) {
    const response = await apiClient.get(`/interviews/sessions/${sessionId}/report`)
    return extractPayload(response)
  },
}

export default interviewApi

