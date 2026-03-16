import jobPostingApi from '../api/jobPostingApi.js'
import CrudWorkspace from '../components/resource/CrudWorkspace.jsx'
import usePageTitle from '../hooks/usePageTitle.js'

function JobPostingPage() {
  usePageTitle('채용공고')

  return (
    <CrudWorkspace
      eyebrow="채용공고 관리"
      title="채용공고 관리"
      description="이력서, 자기소개서, 면접 준비의 기준이 되는 채용공고를 저장합니다."
      api={jobPostingApi}
      createEmptyItem={() => ({
        companyName: '',
        positionTitle: '',
        description: '',
        jobUrl: '',
        deadline: '',
      })}
      fields={[
        { name: 'companyName', label: '회사명', placeholder: '카카오뱅크' },
        { name: 'positionTitle', label: '직무명', placeholder: '플랫폼 백엔드 엔지니어' },
        {
          name: 'description',
          label: '상세 내용',
          type: 'textarea',
          rows: 8,
          placeholder: '주요 업무, 자격 요건, 우대 사항을 입력하세요.',
        },
        { name: 'jobUrl', label: '공고 URL', placeholder: 'https://example.com/jobs/backend' },
        { name: 'deadline', label: '마감일', placeholder: '2026-03-31' },
      ]}
      emptyTitle="아직 채용공고가 없습니다."
      emptyDescription="링크나 복사한 텍스트로 첫 공고를 추가하세요."
      listItemTitle={(item) => item.positionTitle}
      listItemMeta={(item) => `${item.companyName || '회사 미지정'} - ${item.deadline || '-'}`}
    />
  )
}

export default JobPostingPage
