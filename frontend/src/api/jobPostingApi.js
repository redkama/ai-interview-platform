import { extractPayload } from './apiUtils.js'
import apiClient from './client.js'
import { createStubCrud } from './stubDatabase.js'

const stubApi = createStubCrud('jobPostings', 'job-posting')
const useStubApi = import.meta.env.VITE_USE_API_STUB !== 'false'

function normalizePayload(payload) {
  return {
    ...payload,
    jobUrl: payload.jobUrl?.trim() ? payload.jobUrl : null,
    deadline: payload.deadline?.trim() ? payload.deadline : null,
  }
}

const jobPostingApi = {
  async list() {
    if (useStubApi) {
      return stubApi.list()
    }

    const response = await apiClient.get('/profiles/job-postings')
    return extractPayload(response)
  },
  async create(payload) {
    if (useStubApi) {
      return stubApi.create(payload)
    }

    const response = await apiClient.post('/profiles/job-postings', normalizePayload(payload))
    return extractPayload(response)
  },
  async update(id, payload) {
    if (useStubApi) {
      return stubApi.update(id, payload)
    }

    const response = await apiClient.put(`/profiles/job-postings/${id}`, normalizePayload(payload))
    return extractPayload(response)
  },
  async remove(id) {
    if (useStubApi) {
      return stubApi.remove(id)
    }

    const response = await apiClient.delete(`/profiles/job-postings/${id}`)
    return extractPayload(response)
  },
}

export default jobPostingApi
