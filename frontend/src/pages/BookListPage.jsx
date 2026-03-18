import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import bookApi from '../api/bookApi.js'
import usePageTitle from '../hooks/usePageTitle.js'

const categories = ['', 'INTERVIEW', 'ENGLISH', 'KOREAN_HISTORY', 'SELF_DEVELOPMENT', 'OTHER']

const categoryLabelMap = {
  '': '전체',
  INTERVIEW: '면접',
  ENGLISH: '영어',
  KOREAN_HISTORY: '한국사',
  SELF_DEVELOPMENT: '자기계발',
  OTHER: '기타',
}

function BookListPage() {
  usePageTitle('도서')
  const [selectedCategory, setSelectedCategory] = useState('')
  const [keyword, setKeyword] = useState('')
  const [books, setBooks] = useState([])

  useEffect(() => {
    bookApi.listBooks({ category: selectedCategory, keyword }).then((response) => {
      setBooks(response.content ?? [])
    })
  }, [selectedCategory, keyword])

  return (
    <section className="workspace-page">
      <div className="workspace-page__hero">
        <p className="page-card__eyebrow">도서 판매</p>
        <h2 className="page-card__title">도서 목록</h2>
        <p className="page-card__description">
          카테고리 필터와 검색으로 도서를 찾고, 상세 페이지에서 구매 흐름을 시연할 수 있습니다.
        </p>
      </div>

      <section className="panel">
        <div className="toolbar-row">
          <div className="tab-row">
            {categories.map((category) => (
              <button
                key={category || 'ALL'}
                className={selectedCategory === category ? 'tab-chip tab-chip--active' : 'tab-chip'}
                type="button"
                onClick={() => setSelectedCategory(category)}
              >
                {categoryLabelMap[category]}
              </button>
            ))}
          </div>

          <input
            className="form-control catalog-search"
            type="search"
            value={keyword}
            onChange={(event) => setKeyword(event.target.value)}
            placeholder="도서명, 저자, 설명으로 검색"
          />
        </div>

        <div className="book-grid">
          {books.map((book) => (
            <article key={book.id} className="book-card">
              <div className="book-card__image-wrap">
                <img className="book-card__image" src={book.imageUrl} alt={book.title} />
              </div>
              <p className="book-card__meta">{categoryLabelMap[book.category] ?? book.category}</p>
              <h3 className="panel__title">{book.title}</h3>
              <p className="panel__subtitle">
                {book.author} · {book.publisher}
              </p>
              <div className="book-card__footer">
                <strong>{book.price.toLocaleString()}원</strong>
                <Link className="button button--secondary" to={`/books/${book.id}`}>
                  상세 보기
                </Link>
              </div>
            </article>
          ))}
        </div>
      </section>
    </section>
  )
}

export default BookListPage
