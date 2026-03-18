import { useEffect, useState } from 'react'
import { useLocation } from 'react-router-dom'
import bookApi from '../api/bookApi.js'
import usePageTitle from '../hooks/usePageTitle.js'

function OrderPage() {
  usePageTitle('주문')
  const location = useLocation()
  const [orders, setOrders] = useState([])
  const [highlightOrder, setHighlightOrder] = useState(null)

  useEffect(() => {
    bookApi.getMyOrders().then((items) => {
      setOrders(items)
      const createdOrderId = location.state?.createdOrderId
      if (createdOrderId) {
        setHighlightOrder(items.find((item) => item.id === createdOrderId) ?? null)
      }
    })
  }, [location.state])

  return (
    <section className="workspace-page">
      <div className="workspace-page__hero">
        <p className="page-card__eyebrow">도서 판매</p>
        <h2 className="page-card__title">주문</h2>
        <p className="page-card__description">
          방금 생성한 주문 확인, 결제 자리표시자, 주문 내역을 한 화면에서 볼 수 있습니다.
        </p>
      </div>

      {highlightOrder ? (
        <section className="panel">
          <div className="panel__header">
            <div>
              <h3 className="panel__title">주문 확인</h3>
              <p className="panel__subtitle">주문번호 #{highlightOrder.id}</p>
            </div>
            <span className="dashboard-score-chip">{highlightOrder.status}</span>
          </div>

          <div className="dashboard-action-list">
            {highlightOrder.items.map((item) => (
              <article key={item.id} className="dashboard-action-item">
                <strong>{item.title}</strong>
                <p>
                  {item.quantity}권 · {item.priceAtOrder.toLocaleString()}원
                </p>
              </article>
            ))}
          </div>

          <div className="cart-summary">
            <strong>{highlightOrder.totalPrice.toLocaleString()}원</strong>
            <button className="button" type="button">
              결제하기 Placeholder
            </button>
          </div>
        </section>
      ) : null}

      <section className="panel">
        <div className="panel__header">
          <div>
            <h3 className="panel__title">내 주문 목록</h3>
            <p className="panel__subtitle">시연용 최근 주문 내역입니다.</p>
          </div>
        </div>

        <div className="dashboard-session-list">
          {orders.map((order) => (
            <article key={order.id} className="dashboard-session-item">
              <div className="dashboard-session-item__row">
                <strong>#{order.id}</strong>
                <span className="dashboard-score-chip">{order.status}</span>
              </div>
              <p className="dashboard-session-item__meta">{new Date(order.orderedAt).toLocaleString()}</p>
              <p className="panel__subtitle">{order.items.map((item) => item.title).join(', ')}</p>
            </article>
          ))}
        </div>
      </section>
    </section>
  )
}

export default OrderPage
