import BaseLayout from '../components/layout/BaseLayout.jsx'
import CoverLetterPage from '../pages/CoverLetterPage.jsx'
import DashboardPage from '../pages/DashboardPage.jsx'
import InterviewResultPage from '../pages/InterviewResultPage.jsx'
import InterviewSessionPage from '../pages/InterviewSessionPage.jsx'
import InterviewSetupPage from '../pages/InterviewSetupPage.jsx'
import JobPostingPage from '../pages/JobPostingPage.jsx'
import LoginPage from '../pages/LoginPage.jsx'
import NotFoundPage from '../pages/NotFoundPage.jsx'
import ResumePage from '../pages/ResumePage.jsx'
import SignupPage from '../pages/SignupPage.jsx'
import { IndexRedirect, ProtectedRoute, PublicOnlyRoute } from './RouteGuards.jsx'

const routes = [
  {
    path: '/',
    element: <BaseLayout />,
    children: [
      { index: true, element: <IndexRedirect /> },
      {
        element: <PublicOnlyRoute />,
        children: [
          { path: 'login', element: <LoginPage /> },
          { path: 'signup', element: <SignupPage /> },
        ],
      },
      {
        element: <ProtectedRoute />,
        children: [
          { path: 'dashboard', element: <DashboardPage /> },
          { path: 'resume', element: <ResumePage /> },
          { path: 'cover-letter', element: <CoverLetterPage /> },
          { path: 'job-posting', element: <JobPostingPage /> },
          { path: 'interview/setup', element: <InterviewSetupPage /> },
          { path: 'interview/session', element: <InterviewSessionPage /> },
          { path: 'interview/result', element: <InterviewResultPage /> },
        ],
      },
      { path: '*', element: <NotFoundPage /> },
    ],
  },
]

export default routes
