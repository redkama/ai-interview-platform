import { useEffect, useMemo, useState } from 'react'
import educationApi from '../api/educationApi.js'
import usePageTitle from '../hooks/usePageTitle.js'

const levelTabs = ['BEGINNER', 'INTERMEDIATE', 'ADVANCED']

const levelLabelMap = {
  BEGINNER: '초급',
  INTERMEDIATE: '중급',
  ADVANCED: '고급',
}

function EnglishPage() {
  usePageTitle('영어 교육')
  const [selectedLevel, setSelectedLevel] = useState('BEGINNER')
  const [lessons, setLessons] = useState([])
  const [progress, setProgress] = useState(null)

  useEffect(() => {
    educationApi.getEnglishProgress().then(setProgress)
  }, [])

  useEffect(() => {
    educationApi.getEnglishLessons(selectedLevel).then(setLessons)
  }, [selectedLevel])

  const progressLabel = useMemo(() => {
    if (!progress) {
      return ''
    }

    return `전체 ${progress.totalLessons}개 중 ${progress.completedLessons}개 완료`
  }, [progress])

  return (
    <section className="workspace-page">
      <div className="workspace-page__hero">
        <p className="page-card__eyebrow">영어 교육</p>
        <h2 className="page-card__title">면접 영어 레슨</h2>
        <p className="page-card__description">
          레벨별로 레슨을 나눠 보고, 실제 면접 전 학습 진행률을 함께 확인할 수 있습니다.
        </p>
      </div>

      <section className="panel">
        <div className="panel__header">
          <div>
            <h3 className="panel__title">학습 진행률</h3>
            <p className="panel__subtitle">{progressLabel}</p>
          </div>
          <strong>{progress?.progressPercent ?? 0}%</strong>
        </div>

        <div className="progress-bar" aria-label="영어 학습 진행률">
          <div className="progress-bar__fill" style={{ width: `${progress?.progressPercent ?? 0}%` }} />
        </div>
      </section>

      <section className="panel">
        <div className="tab-row" role="tablist" aria-label="영어 레벨 필터">
          {levelTabs.map((level) => (
            <button
              key={level}
              className={selectedLevel === level ? 'tab-chip tab-chip--active' : 'tab-chip'}
              type="button"
              onClick={() => setSelectedLevel(level)}
            >
              {levelLabelMap[level]}
            </button>
          ))}
        </div>

        <div className="lesson-grid">
          {lessons.map((lesson) => (
            <article key={lesson.id} className="lesson-card">
              <p className="lesson-card__meta">
                {levelLabelMap[lesson.level]} · {lesson.part}
              </p>
              <h3 className="panel__title">{lesson.title}</h3>
              <p className="panel__subtitle">{lesson.content}</p>
            </article>
          ))}
        </div>
      </section>
    </section>
  )
}

export default EnglishPage
