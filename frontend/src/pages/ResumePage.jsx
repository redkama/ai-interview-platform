import resumeApi from '../api/resumeApi.js'
import CrudWorkspace from '../components/resource/CrudWorkspace.jsx'
import usePageTitle from '../hooks/usePageTitle.js'

function ResumePage() {
  usePageTitle('이력서')

  return (
    <CrudWorkspace
      eyebrow="이력서 관리"
      title="이력서 관리"
      description="지원 직무별로 여러 이력서를 관리하고 면접 세션에서 바로 선택할 수 있습니다."
      api={resumeApi}
      createEmptyItem={() => ({
        title: '',
        content: '',
      })}
      fields={[
        { name: 'title', label: '이력서 제목', placeholder: '백엔드 개발자 이력서' },
        {
          name: 'content',
          label: '내용',
          type: 'textarea',
          rows: 10,
          placeholder: '경험, 프로젝트, 기술 스택, 성과를 정리해 주세요.',
        },
      ]}
      emptyTitle="아직 이력서가 없습니다."
      emptyDescription="첫 이력서를 만들어 면접 준비를 시작해 보세요."
      listItemTitle={(item) => item.title}
      listItemMeta={(item) => item.updatedAt || '-'}
    />
  )
}

export default ResumePage

