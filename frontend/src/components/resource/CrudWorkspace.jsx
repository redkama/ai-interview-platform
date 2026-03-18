import useCrudResource from '../../hooks/useCrudResource.js'
import ErrorBlock from '../common/ErrorBlock.jsx'
import LoadingBlock from '../common/LoadingBlock.jsx'
import StatusMessage from '../common/StatusMessage.jsx'
import SelectField from '../forms/SelectField.jsx'
import TextAreaField from '../forms/TextAreaField.jsx'
import TextInput from '../forms/TextInput.jsx'

function renderField(field, value, onChange) {
  const sharedProps = {
    label: field.label,
    name: field.name,
    value: value ?? '',
    onChange: (event) => onChange(field.name, event.target.value),
    placeholder: field.placeholder,
    hint: field.hint,
  }

  if (field.type === 'textarea') {
    return <TextAreaField key={field.name} rows={field.rows} {...sharedProps} />
  }

  if (field.type === 'select') {
    return <SelectField key={field.name} options={field.options} {...sharedProps} />
  }

  return <TextInput key={field.name} type="text" {...sharedProps} />
}

function CrudWorkspace({
  eyebrow,
  title,
  description,
  api,
  createEmptyItem,
  fields,
  emptyTitle,
  emptyDescription,
  listItemTitle,
  listItemMeta,
}) {
  const {
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
  } = useCrudResource({ api, createEmptyItem })

  return (
    <section className="workspace-page">
      <div className="workspace-page__hero">
        <p className="page-card__eyebrow">{eyebrow}</p>
        <h2 className="page-card__title">{title}</h2>
        <p className="page-card__description">{description}</p>
      </div>

      <StatusMessage variant="error" message={error} />
      <StatusMessage variant="success" message={successMessage} />

      <div className="workspace-grid">
        <aside className="panel">
          <div className="panel__header">
            <div>
              <h3 className="panel__title">목록</h3>
              <p className="panel__subtitle">{items.length}건</p>
            </div>
            <button className="button button--ghost" type="button" onClick={startCreate}>
              새 항목
            </button>
          </div>

          {isLoading ? <LoadingBlock label="항목을 불러오는 중입니다." /> : null}
          {!isLoading && error ? <ErrorBlock message={error} onRetry={loadItems} /> : null}

          {!isLoading && !error && items.length === 0 ? (
            <div className="empty-card">
              <h4>{emptyTitle}</h4>
              <p>{emptyDescription}</p>
            </div>
          ) : null}

          {!isLoading && !error && items.length > 0 ? (
            <div className="resource-list">
              {items.map((item) => (
                <button
                  key={item.id}
                  type="button"
                  className={
                    item.id === selectedId
                      ? 'resource-list__item resource-list__item--active'
                      : 'resource-list__item'
                  }
                  onClick={() => selectItem(item.id)}
                >
                  <strong>{listItemTitle(item)}</strong>
                  <span>{listItemMeta(item)}</span>
                </button>
              ))}
            </div>
          ) : null}
        </aside>

        <section className="panel">
          <div className="panel__header">
            <div>
              <h3 className="panel__title">{selectedId ? '항목 수정' : '항목 생성'}</h3>
              <p className="panel__subtitle">오른쪽 편집기에서 바로 내용을 저장할 수 있습니다.</p>
            </div>
          </div>

          <div className="editor-form">
            {fields.map((field) => renderField(field, formData[field.name], updateField))}
          </div>

          <div className="button-row">
            <button className="button" type="button" onClick={saveItem} disabled={isSaving || isLoading}>
              {isSaving ? '저장 중...' : '저장'}
            </button>
            <button className="button button--secondary" type="button" onClick={startCreate} disabled={isSaving}>
              초기화
            </button>
            <button className="button button--danger" type="button" onClick={deleteItem} disabled={isSaving}>
              삭제
            </button>
          </div>
        </section>
      </div>
    </section>
  )
}

export default CrudWorkspace

