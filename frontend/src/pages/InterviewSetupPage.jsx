import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import SelectField from '../components/forms/SelectField.jsx'
import usePageTitle from '../hooks/usePageTitle.js'
import useAppStore from '../store/useAppStore.js'

function InterviewSetupPage() {
  usePageTitle('면접 설정')
  const navigate = useNavigate()
  const startInterview = useAppStore((state) => state.startInterview)
  const [mode, setMode] = useState('general')

  function handleStart() {
    startInterview(mode)
    navigate('/interview/session')
  }

  return (
    <section className="workspace-page">
      <div className="workspace-page__hero">
        <p className="page-card__eyebrow">면접 설정</p>
        <h2 className="page-card__title">모드를 선택하고 모의 면접을 시작하세요.</h2>
        <p className="page-card__description">
          첫 단계를 단순하게 유지해 두어 나중에 백엔드 생성 질문 세트와 쉽게 연결할 수 있습니다.
        </p>
      </div>

      <section className="panel interview-setup-card">
        <SelectField
          label="면접 모드"
          value={mode}
          onChange={(event) => setMode(event.target.value)}
          options={[
            { value: 'general', label: '일반' },
            { value: 'behavioral', label: '인성' },
            { value: 'technical', label: '기술' },
          ]}
        />

        <div className="button-row">
          <button className="button" type="button" onClick={handleStart}>
            면접 시작
          </button>
        </div>
      </section>
    </section>
  )
}

export default InterviewSetupPage
