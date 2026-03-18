import { delay } from './apiUtils.js'
import apiClient from './client.js'

const useStubApi = import.meta.env.VITE_USE_API_STUB === 'true'

const bookCatalog = [
  { id: 'book-1', title: '면접 전략 플레이북', author: '에이 멘토', publisher: 'AI 멘토 프레스', price: 22000, stock: 12, description: '기술 면접과 인성 면접 준비 흐름을 실제 예시 중심으로 정리한 책입니다.', imageUrl: 'https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=900&q=80', category: 'INTERVIEW' },
  { id: 'book-2', title: '취업 영어 답변 패턴', author: '박클레어', publisher: 'AI 멘토 프레스', price: 18000, stock: 8, description: '면접과 네트워킹 상황에서 바로 쓸 수 있는 영어 답변 구조를 담았습니다.', imageUrl: 'https://images.unsplash.com/photo-1495446815901-a7297e633e8d?auto=format&fit=crop&w=900&q=80', category: 'ENGLISH' },
  { id: 'book-3', title: '한국사 핵심 압축 노트', author: '이민', publisher: '히스토리 랩', price: 19500, stock: 6, description: '시대별 흐름과 시험 포인트를 함께 정리한 한국사 요약서입니다.', imageUrl: 'https://images.unsplash.com/photo-1521587760476-6c12a4b040da?auto=format&fit=crop&w=900&q=80', category: 'KOREAN_HISTORY' },
  { id: 'book-4', title: '꾸준함을 만드는 자기계발', author: '한지우', publisher: '그로스 노트', price: 16800, stock: 15, description: '학습 루틴과 면접 준비 습관을 오래 유지하는 방법을 정리했습니다.', imageUrl: 'https://images.unsplash.com/photo-1516979187457-637abb4f9353?auto=format&fit=crop&w=900&q=80', category: 'SELF_DEVELOPMENT' },
]

let cart = { id: 'cart-1', items: [] }

let orders = [
  {
    id: 'order-1001',
    totalPrice: 22000,
    status: 'DELIVERED',
    orderedAt: '2026-03-10T09:00:00',
    items: [{ id: 'order-item-1', bookId: 'book-1', title: '면접 전략 플레이북', quantity: 1, priceAtOrder: 22000, linePrice: 22000 }],
  },
]

function resolveBaseUrl(path) {
  if (typeof window === 'undefined') {
    return path
  }

  return new URL(path, window.location.origin).toString()
}

function mapCart() {
  return { ...cart, totalPrice: cart.items.reduce((sum, item) => sum + item.linePrice, 0) }
}

function findBook(bookId) {
  return bookCatalog.find((item) => String(item.id) === String(bookId))
}

const bookApi = {
  async listBooks({ category = '', keyword = '', page = 0, size = 12 } = {}) {
    if (useStubApi) {
      await delay(250)
      const normalizedKeyword = keyword.trim().toLowerCase()
      let filtered = bookCatalog

      if (category) {
        filtered = filtered.filter((book) => book.category === category)
      }

      if (normalizedKeyword) {
        filtered = filtered.filter((book) =>
          book.title.toLowerCase().includes(normalizedKeyword) ||
          book.author.toLowerCase().includes(normalizedKeyword) ||
          book.description.toLowerCase().includes(normalizedKeyword),
        )
      }

      return {
        content: filtered.slice(page * size, page * size + size),
        page,
        size,
        totalElements: filtered.length,
        totalPages: Math.max(1, Math.ceil(filtered.length / size)),
      }
    }

    const query = new URLSearchParams({ page: String(page), size: String(size) })
    if (category) {
      query.set('category', category)
    }
    if (keyword) {
      const response = await apiClient.get(resolveBaseUrl(`/api/books/search?q=${encodeURIComponent(keyword)}`))
      return response.data?.data ?? response.data
    }

    const response = await apiClient.get(resolveBaseUrl(`/api/books?${query.toString()}`))
    return response.data?.data ?? response.data
  },

  async getBook(id) {
    if (useStubApi) {
      await delay(180)
      return findBook(id)
    }

    const response = await apiClient.get(resolveBaseUrl(`/api/books/${id}`))
    return response.data?.data ?? response.data
  },

  async getCart() {
    if (useStubApi) {
      await delay(150)
      return mapCart()
    }

    const response = await apiClient.get(resolveBaseUrl('/api/cart'))
    return response.data?.data ?? response.data
  },

  async addCartItem({ bookId, quantity }) {
    if (useStubApi) {
      await delay(160)
      const book = findBook(bookId)
      const existingItem = cart.items.find((item) => item.bookId === bookId)

      if (existingItem) {
        existingItem.quantity += quantity
        existingItem.linePrice = existingItem.quantity * existingItem.price
      } else {
        cart.items.push({ id: `cart-item-${Date.now()}`, bookId: book.id, title: book.title, price: book.price, quantity, linePrice: book.price * quantity, imageUrl: book.imageUrl })
      }

      return mapCart()
    }

    const response = await apiClient.post(resolveBaseUrl('/api/cart/items'), { bookId, quantity })
    return response.data?.data ?? response.data
  },

  async updateCartItem(id, quantity) {
    if (useStubApi) {
      await delay(140)
      cart.items = cart.items.map((item) => (item.id === id ? { ...item, quantity, linePrice: item.price * quantity } : item))
      return mapCart()
    }

    const response = await apiClient.patch(resolveBaseUrl(`/api/cart/items/${id}`), { quantity })
    return response.data?.data ?? response.data
  },

  async removeCartItem(id) {
    if (useStubApi) {
      await delay(120)
      cart.items = cart.items.filter((item) => item.id !== id)
      return mapCart()
    }

    const response = await apiClient.delete(resolveBaseUrl(`/api/cart/items/${id}`))
    return response.data?.data ?? response.data
  },

  async createOrder() {
    if (useStubApi) {
      await delay(260)
      const nextOrder = {
        id: `order-${Date.now()}`,
        totalPrice: cart.items.reduce((sum, item) => sum + item.linePrice, 0),
        status: 'PENDING',
        orderedAt: new Date().toISOString(),
        items: cart.items.map((item, index) => ({ id: `order-item-${Date.now()}-${index}`, bookId: item.bookId, title: item.title, quantity: item.quantity, priceAtOrder: item.price, linePrice: item.linePrice })),
      }
      orders.unshift(nextOrder)
      cart = { ...cart, items: [] }
      return nextOrder
    }

    const response = await apiClient.post(resolveBaseUrl('/api/orders'))
    return response.data?.data ?? response.data
  },

  async getMyOrders() {
    if (useStubApi) {
      await delay(180)
      return orders
    }

    const response = await apiClient.get(resolveBaseUrl('/api/orders/my'))
    return response.data?.data ?? response.data
  },

  async getOrder(id) {
    if (useStubApi) {
      await delay(150)
      return orders.find((order) => String(order.id) === String(id)) ?? null
    }

    const response = await apiClient.get(resolveBaseUrl(`/api/orders/${id}`))
    return response.data?.data ?? response.data
  },
}

export default bookApi