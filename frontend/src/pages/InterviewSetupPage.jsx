import { useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import interviewApi from '../api/interviewApi.js'
import resumeApi from '../api/resumeApi.js'
import coverLetterApi from '../api/coverLetterApi.js'
import jobPostingApi from '../api/jobPostingApi.js'
import SelectField from '../components/forms/SelectField.jsx'
import TextInput from '../components/forms/TextInput.jsx'
import StatusMessage from '../components/common/StatusMessage.jsx'
import usePageTitle from '../hooks/usePageTitle.js'
import useAppStore from '../store/useAppStore.js'

function InterviewSetupPage() {
  usePageTitle('면접 설정')
  const navigate = useNavigate()
  const setCurrentInterviewSessionId = useAppStore((state) => state.setCurrentInterviewSessionId)
  const [resumes, setResumes] = useState([])
  const [coverLetters, setCoverLetters] = useState([])
  const [jobPostings, setJobPostings] = useState([])
  const [form, setForm] = useState({
    title: '백엔드 실전 모의 면접',
    positionTitle: '백엔드 엔지니어',
    resumeId: '',
    coverLetterId: '',
    jobPostingId: '',
    questionCount: '5',
  })
  const [error, setError] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)

  useEffect(() => {
    async function loadResources() {
      const [resumeItems, coverLetterItems, jobPostingItems] = await Promise.all([
        resumeApi.list(),
        coverLetterApi.list(),
        jobPostingApi.list(),
      ])

      setResumes(resumeItems)
      setCoverLetters(coverLetterItems)
      setJobPostings(jobPostingItems)
      setForm((current) => ({
        ...current,
        resumeId: current.resumeId || String(resumeItems[0]?.id ?? ''),
        coverLetterId: current.coverLetterId || String(coverLetterItems[0]?.id ?? ''),
        jobPostingId: current.jobPostingId || String(jobPostingItems[0]?.id ?? ''),
      }))
    }

    loadResources().catch(() => {
      setError('면접에 사용할 문서를 불러오지 못했습니다.')
    })
  }, [])

  const canStart = useMemo(
    () => Boolean(form.title && form.positionTitle && form.resumeId && form.coverLetterId && form.jobPostingId),
    [form],
  )

  function updateField(name, value) {
    setForm((current) => ({
      ...current,
      [name]: value,
    }))
  }

  async function handleStart() {
    if (!canStart) {
      setError('이력서, 자기소개서, 채용공고를 모두 선택해 주세요.')
      return
    }

    setError('')
    setIsSubmitting(true)
    try {
      const session = await interviewApi.startSession({
        title: form.title,
        positionTitle: form.positionTitle,
        resumeId: Number(form.resumeId),
        coverLetterId: Number(form.coverLetterId),
        jobPostingId: Number(form.jobPostingId),
        questionCount: Number(form.questionCount),
      })
      setCurrentInterviewSessionId(session.id)
      navigate('/interview/session')
    } catch (requestError) {
      setError(requestError.message ?? '면접 세션 생성에 실패했습니다.')
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <section className="workspace-page">
      <div className="workspace-page__hero">
        <p className="page-card__eyebrow">면접 설정</p>
        <h2 className="page-card__title">실제 AI 모의 면접 세션을 시작합니다</h2>
        <p className="page-card__description">
          저장된 이력서, 자기소개서, 채용공고를 바탕으로 백엔드가 AI 서비스에 질문 생성을 요청합니다.
        </p>
      </div>

      <section className="panel interview-setup-card">
        <StatusMessage variant="error" message={error} />

        <div className="editor-form">
          <TextInput
            label="세션 제목"
            value={form.title}
            onChange={(event) => updateField('title', event.target.value)}
            placeholder="백엔드 실전 모의 면접"
          />
          <TextInput
            label="지원 직무"
            value={form.positionTitle}
            onChange={(event) => updateField('positionTitle', event.target.value)}
            placeholder="백엔드 엔지니어"
          />
          <SelectField
            label="이력서"
            value={form.resumeId}
            onChange={(event) => updateField('resumeId', event.target.value)}
            options={[
              { value: '', label: '이력서를 선택하세요' },
              ...resumes.map((item) => ({ value: String(item.id), label: item.title })),
            ]}
          />
          <SelectField
            label="자기소개서"
            value={form.coverLetterId}
            onChange={(event) => updateField('coverLetterId', event.target.value)}
            options={[
              { value: '', label: '자기소개서를 선택하세요' },
              ...coverLetters.map((item) => ({ value: String(item.id), label: item.title })),
            ]}
          />
          <SelectField
            label="채용공고"
            value={form.jobPostingId}
            onChange={(event) => updateField('jobPostingId', event.target.value)}
            options={[
              { value: '', label: '채용공고를 선택하세요' },
              ...jobPostings.map((item) => ({ value: String(item.id), label: `${item.companyName} · ${item.positionTitle}` })),
            ]}
          />
          <SelectField
            label="질문 개수"
            value={form.questionCount}
            onChange={(event) => updateField('questionCount', event.target.value)}
            options={[
              { value: '3', label: '3개' },
              { value: '5', label: '5개' },
            ]}
          />
        </div>

        <div className="button-row">
          <button className="button" type="button" onClick={handleStart} disabled={!canStart || isSubmitting}>
            {isSubmitting ? '세션 생성 중...' : 'AI 면접 시작'}
          </button>
        </div>
      </section>
    </section>
  )
}

export default InterviewSetupPage

