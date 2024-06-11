import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { HomePage } from './pages/HomePage';
import { ResultPage } from '@/pages/ResultPage/ResultPage';
import { IndexPage } from '@/pages/IndexPage';
import { NotFoundPage } from '@/pages/NotFoundPage';

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
    ],
  },
]);

export function Router() {
  return <RouterProvider router={router} />;
}
