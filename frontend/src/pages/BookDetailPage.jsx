import { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import bookApi from '../api/bookApi.js'
import usePageTitle from '../hooks/usePageTitle.js'

const categoryLabelMap = {
  INTERVIEW: '면접',
  ENGLISH: '영어',
  KOREAN_HISTORY: '한국사',
  SELF_DEVELOPMENT: '자기계발',
  OTHER: '기타',
}

function BookDetailPage() {
  usePageTitle('도서 상세')
  const navigate = useNavigate()
  const { id } = useParams()
  const [book, setBook] = useState(null)
  const [quantity, setQuantity] = useState(1)
  const [successMessage, setSuccessMessage] = useState('')

  useEffect(() => {
    bookApi.getBook(id).then(setBook)
  }, [id])

  async function handleAddToCart() {
    await bookApi.addCartItem({ bookId: id, quantity })
    setSuccessMessage('장바구니에 담았습니다.')
  }

  if (!book) {
    return null
  }

  return (
    <section className="workspace-page">
      <div className="workspace-page__hero">
        <p className="page-card__eyebrow">도서 판매</p>
        <h2 className="page-card__title">도서 상세</h2>
      </div>

      <section className="panel book-detail-layout">
        <div className="book-detail-layout__image-wrap">
          <img className="book-detail-layout__image" src={book.imageUrl} alt={book.title} />
        </div>

        <div className="book-detail-layout__content">
          <p className="book-card__meta">{categoryLabelMap[book.category] ?? book.category}</p>
          <h3 className="page-card__title">{book.title}</h3>
          <p className="panel__subtitle">
            {book.author} · {book.publisher}
          </p>
          <p className="book-detail-copy">{book.description}</p>

          <div className="book-detail-stats">
            <span className="dashboard-score-chip">재고 {book.stock}권</span>
            <strong>{book.price.toLocaleString()}원</strong>
          </div>

          <div className="book-detail-actions">
            <label className="form-field">
              <span className="form-field__label">수량</span>
              <input
                className="form-control quantity-input"
                type="number"
                min="1"
                max={book.stock}
                value={quantity}
                onChange={(event) => setQuantity(Number(event.target.value))}
              />
            </label>

            <div className="button-row">
              <button className="button" type="button" onClick={handleAddToCart}>
                장바구니 담기
              </button>
              <button className="button button--secondary" type="button" onClick={() => navigate('/cart')}>
                장바구니로 이동
              </button>
            </div>

            {successMessage ? <div className="status-message status-message--success">{successMessage}</div> : null}
          </div>
        </div>
      </section>
    </section>
  )
}

export default BookDetailPage
