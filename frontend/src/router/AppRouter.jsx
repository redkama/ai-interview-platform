import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import routes from './routes.jsx'

const router = createBrowserRouter(routes)

function AppRouter() {
  return <RouterProvider router={router} />
}

export default AppRouter
