import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import { HomePage } from './pages/Home.page';
import { ResultPage } from '@/pages/Result.page';
import { IndexPage } from '@/pages/Index.page';
import { NotFoundPage } from '@/pages/NotFound.page';

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
