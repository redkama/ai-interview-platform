import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import authApi from '../api/authApi.js'
import StatusMessage from '../components/common/StatusMessage.jsx'
import TextInput from '../components/forms/TextInput.jsx'
import usePageTitle from '../hooks/usePageTitle.js'
import useAppStore from '../store/useAppStore.js'

function SignupPage() {
  usePageTitle('회원가입')
  const navigate = useNavigate()
  const setCurrentUser = useAppStore((state) => state.setCurrentUser)
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    confirmPassword: '',
  })
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [isSubmitting, setIsSubmitting] = useState(false)

  function updateField(name, value) {
    setFormData((current) => ({
      ...current,
      [name]: value,
    }))
  }

  async function handleSubmit(event) {
    event.preventDefault()
    setError('')
    setSuccess('')

    if (formData.password !== formData.confirmPassword) {
      setError('비밀번호 확인이 일치하지 않습니다.')
      return
    }

    setIsSubmitting(true)

    try {
      const result = await authApi.signup({
        name: formData.name,
        email: formData.email,
        password: formData.password,
      })
      setCurrentUser(result.user)
      setSuccess('회원가입이 완료되었습니다. 대시보드로 이동합니다.')
      setTimeout(() => navigate('/dashboard'), 800)
    } catch (signupError) {
      setError(signupError.message ?? '회원가입에 실패했습니다.')
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <section className="auth-page">
      <div className="auth-panel auth-panel--hero">
        <p className="page-card__eyebrow">시작하기</p>
        <h2 className="page-card__title">면접 준비를 위한 작업 공간을 만들어 보세요.</h2>
        <p className="page-card__description">
          이력서, 자기소개서, 채용공고, 면접 연습까지 하나의 흐름으로 이어지는 시작점입니다.
        </p>
      </div>

      <form className="auth-panel auth-panel--form" onSubmit={handleSubmit}>
        <div className="panel__header">
          <div>
            <h3 className="panel__title">회원가입</h3>
            <p className="panel__subtitle">계정을 만들고 문서 관리와 학습 기능을 바로 시작하세요.</p>
          </div>
        </div>

        <StatusMessage variant="error" message={error} />
        <StatusMessage variant="success" message={success} />

        <div className="editor-form">
          <TextInput
            label="이름"
            name="name"
            value={formData.name}
            onChange={(event) => updateField('name', event.target.value)}
            placeholder="이름을 입력하세요"
          />
          <TextInput
            label="이메일"
            name="email"
            value={formData.email}
            onChange={(event) => updateField('email', event.target.value)}
            placeholder="name@example.com"
          />
          <TextInput
            label="비밀번호"
            type="password"
            name="password"
            value={formData.password}
            onChange={(event) => updateField('password', event.target.value)}
            placeholder="8자 이상 입력하세요"
          />
          <TextInput
            label="비밀번호 확인"
            type="password"
            name="confirmPassword"
            value={formData.confirmPassword}
            onChange={(event) => updateField('confirmPassword', event.target.value)}
            placeholder="비밀번호를 다시 입력하세요"
          />
        </div>

        <div className="button-row">
          <button className="button" type="submit" disabled={isSubmitting}>
            {isSubmitting ? '계정 생성 중...' : '계정 만들기'}
          </button>
        </div>

        <p className="auth-panel__footnote">
          이미 계정이 있다면 <Link to="/login">로그인</Link>
        </p>
      </form>
    </section>
  )
}

export default SignupPage
