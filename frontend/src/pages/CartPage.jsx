import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import bookApi from '../api/bookApi.js'
import usePageTitle from '../hooks/usePageTitle.js'

function CartPage() {
  usePageTitle('장바구니')
  const navigate = useNavigate()
  const [cart, setCart] = useState(null)

  useEffect(() => {
    bookApi.getCart().then(setCart)
  }, [])

  async function handleQuantityChange(itemId, quantity) {
    const nextCart = await bookApi.updateCartItem(itemId, Number(quantity))
    setCart(nextCart)
  }

  async function handleDelete(itemId) {
    const nextCart = await bookApi.removeCartItem(itemId)
    setCart(nextCart)
  }

  async function handleOrder() {
    const order = await bookApi.createOrder()
    navigate('/orders', { state: { createdOrderId: order.id } })
  }

  if (!cart) {
    return null
  }

  return (
    <section className="workspace-page">
      <div className="workspace-page__hero">
        <p className="page-card__eyebrow">도서 판매</p>
        <h2 className="page-card__title">장바구니</h2>
      </div>

      <section className="panel">
        <div className="cart-list">
          {cart.items.length === 0 ? (
            <div className="empty-card">
              <h4>장바구니가 비어 있습니다</h4>
              <p>도서 목록에서 책을 담아 구매 흐름을 이어가 보세요.</p>
            </div>
          ) : (
            cart.items.map((item) => (
              <article key={item.id} className="cart-item">
                <div className="cart-item__main">
                  <img className="cart-item__image" src={item.imageUrl} alt={item.title} />
                  <div>
                    <h3 className="panel__title">{item.title}</h3>
                    <p className="panel__subtitle">{item.price.toLocaleString()}원</p>
                  </div>
                </div>

                <div className="cart-item__controls">
                  <input
                    className="form-control quantity-input"
                    type="number"
                    min="1"
                    value={item.quantity}
                    onChange={(event) => handleQuantityChange(item.id, event.target.value)}
                  />
                  <strong>{item.linePrice.toLocaleString()}원</strong>
                  <button className="button button--danger" type="button" onClick={() => handleDelete(item.id)}>
                    삭제
                  </button>
                </div>
              </article>
            ))
          )}
        </div>

        <div className="cart-summary">
          <div>
            <h3 className="panel__title">총 금액</h3>
            <p className="panel__subtitle">현재 장바구니 수량 기준으로 계산됩니다.</p>
          </div>
          <strong>{cart.totalPrice.toLocaleString()}원</strong>
          <button className="button" type="button" disabled={cart.items.length === 0} onClick={handleOrder}>
            주문하기
          </button>
        </div>
      </section>
    </section>
  )
}

export default CartPage
