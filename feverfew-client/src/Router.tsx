import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { HomePage } from './pages/HomePage/HomePage';
import { ResultPage } from '@/pages/ResultPage/ResultPage';
import { IndexPage } from '@/pages/IndexPage';
import { NotFoundPage } from '@/pages/NotFoundPage';
import { ResultArchivePage } from '@/pages/ResultPage/ResultArchivePage';

const router = createBrowserRouter([
  {
    path: '/',
    element: <IndexPage />,
    children: [
      {
        index: true,
        element: <HomePage />,
      },
      {
        path: '/*',
        element: <NotFoundPage />,
      },
      {
        path: '/check',
        element: <ResultPage />,
      },
      {
        path: '/check/archive/:id',
        element: <ResultArchivePage />,
      },
    ],
  },
]);

export function Router() {
  return <RouterProvider router={router} />;
}
