function PagePlaceholder({ title, description, highlights }) {
  return (
    <section className="page-card">
      <div className="page-card__content">
        <p className="page-card__eyebrow">준비 중 페이지</p>
        <h2 className="page-card__title">{title}</h2>
        <p className="page-card__description">{description}</p>
      </div>

      <div className="page-card__panel">
        <h3 className="page-card__panel-title">예정 범위</h3>
        <ul className="page-card__list">
          {highlights.map((item) => (
            <li key={item}>{item}</li>
          ))}
        </ul>
      </div>
    </section>
  )
}

export default PagePlaceholder
