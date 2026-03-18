import {
  CartesianGrid,
  Line,
  LineChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from 'recharts'
import { useEffect, useState } from 'react'
import dashboardApi from '../api/dashboardApi.js'
import usePageTitle from '../hooks/usePageTitle.js'

function DashboardPage() {
  usePageTitle('대시보드')
  const [summary, setSummary] = useState(null)

  useEffect(() => {
    dashboardApi.getSummary().then(setSummary)
  }, [])

  if (!summary) {
    return null
  }

  return (
    <section className="workspace-page">
      <div className="workspace-page__hero">
        <p className="page-card__eyebrow">대시보드</p>
        <h2 className="page-card__title">면접, 학습, 주문 활동을 한눈에 보여줍니다</h2>
        <p className="page-card__description">
          포트폴리오 시연용으로 면접 점수 변화, 학습 진행률, 최근 주문, 추천 콘텐츠를 함께 구성했습니다.
        </p>
      </div>

      <div className="dashboard-grid">
        <section className="panel dashboard-chart-card">
          <div className="panel__header">
            <div>
              <h3 className="panel__title">면접 점수 변화</h3>
              <p className="panel__subtitle">최근 연습 세션의 점수 흐름입니다.</p>
            </div>
          </div>

          <div className="dashboard-chart">
            <ResponsiveContainer width="100%" height="100%">
              <LineChart
                data={summary.interviewScoreTrend}
                margin={{ top: 8, right: 8, left: -16, bottom: 0 }}
              >
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
              <h3 className="panel__title">학습 진행률</h3>
              <p className="panel__subtitle">영어와 한국사 학습 현황을 보여줍니다.</p>
            </div>
          </div>

          <div className="progress-stack">
            <div>
              <div className="progress-stack__row">
                <strong>영어</strong>
                <span>{summary.learningProgress.english}%</span>
              </div>
              <div className="progress-bar">
                <div
                  className="progress-bar__fill"
                  style={{ width: `${summary.learningProgress.english}%` }}
                />
              </div>
            </div>
            <div>
              <div className="progress-stack__row">
                <strong>한국사</strong>
                <span>{summary.learningProgress.history}%</span>
              </div>
              <div className="progress-bar">
                <div
                  className="progress-bar__fill"
                  style={{ width: `${summary.learningProgress.history}%` }}
                />
              </div>
            </div>
          </div>
        </section>

        <section className="panel dashboard-recent-card">
          <div className="panel__header">
            <div>
              <h3 className="panel__title">최근 주문 내역</h3>
              <p className="panel__subtitle">최근 3개의 구매 기록입니다.</p>
            </div>
          </div>

          <div className="dashboard-session-list">
            {summary.recentOrders.map((order) => (
              <article key={order.id} className="dashboard-session-item">
                <div className="dashboard-session-item__row">
                  <strong>#{order.id}</strong>
                  <span className="dashboard-score-chip">{order.status}</span>
                </div>
                <p className="dashboard-session-item__meta">
                  {new Date(order.orderedAt).toLocaleDateString()}
                </p>
                <p className="panel__subtitle">
                  {order.items.map((item) => item.title).join(', ')}
                </p>
              </article>
            ))}
          </div>
        </section>

        <section className="panel">
          <div className="panel__header">
            <div>
              <h3 className="panel__title">추천 콘텐츠</h3>
              <p className="panel__subtitle">다음 액션을 설명하기 쉬운 카드 형태로 정리했습니다.</p>
            </div>
          </div>

          <div className="dashboard-action-list">
            {summary.recommendations.map((item) => (
              <article key={item.id} className="dashboard-action-item">
                <strong>{item.title}</strong>
                <p>{item.description}</p>
                <p className="dashboard-session-item__meta">{item.label}</p>
              </article>
            ))}
          </div>
        </section>
      </div>
    </section>
  )
}

export default DashboardPage
