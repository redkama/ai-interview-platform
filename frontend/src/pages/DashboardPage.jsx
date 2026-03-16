import {
  CartesianGrid,
  Line,
  LineChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from 'recharts'
import usePageTitle from '../hooks/usePageTitle.js'
import useAppStore from '../store/useAppStore.js'

function DashboardPage() {
  usePageTitle('대시보드')
  const recentInterviewSessions = useAppStore((state) => state.recentInterviewSessions)
  const scoreTrend = useAppStore((state) => state.scoreTrend)
  const weaknessTags = useAppStore((state) => state.weaknessTags)
  const recommendedNextActions = useAppStore((state) => state.recommendedNextActions)

  return (
    <section className="workspace-page">
      <div className="workspace-page__hero">
        <p className="page-card__eyebrow">성장 대시보드</p>
        <h2 className="page-card__title">최근 면접 연습 흐름을 한눈에 확인하세요.</h2>
        <p className="page-card__description">
          이 페이지는 요약 정보를 읽기 쉽게 유지하면서도 백엔드 API와 연결하기 쉽게 설계되었습니다.
        </p>
      </div>

      <div className="dashboard-grid">
        <section className="panel dashboard-chart-card">
          <div className="panel__header">
            <div>
              <h3 className="panel__title">점수 추이</h3>
              <p className="panel__subtitle">최근 면접 점수 변화를 시간순으로 보여줍니다.</p>
            </div>
          </div>

          <div className="dashboard-chart">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={scoreTrend} margin={{ top: 8, right: 8, left: -16, bottom: 0 }}>
                <CartesianGrid strokeDasharray="3 3" stroke="rgba(18, 18, 18, 0.08)" />
                <XAxis dataKey="date" tickLine={false} axisLine={false} />
                <YAxis domain={[60, 100]} tickLine={false} axisLine={false} width={36} />
                <Tooltip />
                <Line
                  type="monotone"
                  dataKey="score"
                  stroke="#121212"
                  strokeWidth={2.5}
                  dot={{ r: 4, fill: '#121212' }}
                  activeDot={{ r: 6 }}
                />
              </LineChart>
            </ResponsiveContainer>
          </div>
        </section>

        <section className="panel">
          <div className="panel__header">
            <div>
              <h3 className="panel__title">보완 태그</h3>
              <p className="panel__subtitle">최근 세션에서 반복해서 나타난 신호입니다.</p>
            </div>
          </div>

          <div className="tag-list">
            {weaknessTags.map((tag) => (
              <span key={tag} className="dashboard-tag">
                {tag}
              </span>
            ))}
          </div>
        </section>

        <section className="panel dashboard-recent-card">
          <div className="panel__header">
            <div>
              <h3 className="panel__title">최근 면접 세션</h3>
              <p className="panel__subtitle">가장 최근 연습 기록과 짧은 메모입니다.</p>
            </div>
          </div>

          <div className="dashboard-session-list">
            {recentInterviewSessions.map((session) => (
              <article key={session.id} className="dashboard-session-item">
                <div className="dashboard-session-item__row">
                  <strong>{session.date}</strong>
                  <span className="dashboard-score-chip">점수 {session.score}</span>
                </div>
                <p className="dashboard-session-item__meta">{session.mode} 면접</p>
                <p className="panel__subtitle">{session.summary}</p>
              </article>
            ))}
          </div>
        </section>

        <section className="panel">
          <div className="panel__header">
            <div>
              <h3 className="panel__title">추천 다음 행동</h3>
              <p className="panel__subtitle">향후 AI 추천이 들어갈 영역입니다.</p>
            </div>
          </div>

          <div className="dashboard-action-list">
            {recommendedNextActions.map((action) => (
              <article key={action} className="dashboard-action-item">
                <strong>다음 단계</strong>
                <p>{action}</p>
              </article>
            ))}
          </div>
        </section>
      </div>
    </section>
  )
}

export default DashboardPage
