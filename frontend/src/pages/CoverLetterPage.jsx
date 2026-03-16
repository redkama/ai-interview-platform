import coverLetterApi from '../api/coverLetterApi.js'
import CrudWorkspace from '../components/resource/CrudWorkspace.jsx'
import usePageTitle from '../hooks/usePageTitle.js'

function CoverLetterPage() {
  usePageTitle('자기소개서')

  return (
    <CrudWorkspace
      eyebrow="자기소개서 관리"
      title="자기소개서 관리"
      description="회사와 직무에 맞춘 자기소개서를 일관된 편집 화면에서 관리할 수 있습니다."
      api={coverLetterApi}
      createEmptyItem={() => ({
        title: '',
        companyName: '',
        content: '',
      })}
      fields={[
        { name: 'title', label: '문서 제목', placeholder: '카카오뱅크 지원 초안' },
        { name: 'companyName', label: '회사명', placeholder: '카카오뱅크' },
        {
          name: 'content',
          label: '내용',
          type: 'textarea',
          rows: 10,
          placeholder: '자기소개서 초안을 여기에 작성하세요.',
        },
      ]}
      emptyTitle="아직 자기소개서가 없습니다."
      emptyDescription="직무별 초안을 저장해 두면 버전 비교가 쉬워집니다."
      listItemTitle={(item) => item.title}
      listItemMeta={(item) => `${item.companyName || '회사 미지정'} - ${item.updatedAt || '-'}`}
    />
  )
}

export default CoverLetterPage
