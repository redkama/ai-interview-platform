import { useEffect, useState } from 'react'

function useCrudResource({ api, createEmptyItem }) {
  const [items, setItems] = useState([])
  const [formData, setFormData] = useState(createEmptyItem())
  const [selectedId, setSelectedId] = useState(null)
  const [isLoading, setIsLoading] = useState(true)
  const [isSaving, setIsSaving] = useState(false)
  const [error, setError] = useState('')
  const [successMessage, setSuccessMessage] = useState('')

  async function loadItems() {
    setIsLoading(true)
    setError('')

    try {
      const nextItems = await api.list()
      setItems(nextItems)

      if (nextItems.length > 0) {
        setSelectedId(nextItems[0].id)
        setFormData(nextItems[0])
      } else {
        setSelectedId(null)
        setFormData(createEmptyItem())
      }
    } catch (loadError) {
      setError(loadError.message ?? '항목을 불러오지 못했습니다.')
    } finally {
      setIsLoading(false)
    }
  }

  useEffect(() => {
    loadItems()
  }, [])

  function selectItem(id) {
    const item = items.find((candidate) => candidate.id === id)

    if (!item) {
      return
    }

    setSelectedId(id)
    setFormData(item)
    setSuccessMessage('')
  }

  function startCreate() {
    setSelectedId(null)
    setFormData(createEmptyItem())
    setSuccessMessage('')
  }

  function updateField(name, value) {
    setFormData((current) => ({
      ...current,
      [name]: value,
    }))
  }

  async function saveItem() {
    setIsSaving(true)
    setError('')
    setSuccessMessage('')

    try {
      if (selectedId) {
        const updated = await api.update(selectedId, formData)
        setItems((current) =>
          current.map((item) => (item.id === selectedId ? updated : item)),
        )
        setFormData(updated)
        setSuccessMessage('변경 사항이 저장되었습니다.')
      } else {
        const created = await api.create(formData)
        setItems((current) => [created, ...current])
        setSelectedId(created.id)
        setFormData(created)
        setSuccessMessage('새 항목이 생성되었습니다.')
      }
    } catch (saveError) {
      setError(saveError.message ?? '항목을 저장하지 못했습니다.')
    } finally {
      setIsSaving(false)
    }
  }

  async function deleteItem() {
    if (!selectedId) {
      startCreate()
      return
    }

    setIsSaving(true)
    setError('')
    setSuccessMessage('')

    try {
      await api.remove(selectedId)
      const nextItems = items.filter((item) => item.id !== selectedId)
      setItems(nextItems)

      if (nextItems.length > 0) {
        setSelectedId(nextItems[0].id)
        setFormData(nextItems[0])
      } else {
        setSelectedId(null)
        setFormData(createEmptyItem())
      }

      setSuccessMessage('항목이 삭제되었습니다.')
    } catch (deleteError) {
      setError(deleteError.message ?? '항목을 삭제하지 못했습니다.')
    } finally {
      setIsSaving(false)
    }
  }

  return {
    items,
    formData,
    selectedId,
    isLoading,
    isSaving,
    error,
    successMessage,
    loadItems,
    selectItem,
    startCreate,
    updateField,
    saveItem,
    deleteItem,
  }
}

export default useCrudResource
