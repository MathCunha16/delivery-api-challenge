import { createBrowserRouter } from 'react-router-dom';
import SelectionPage from '../pages/SelectionPage';
import DashboardPage from '../pages/DashboardPage';

export const router = createBrowserRouter([
    {
        path: '/',
        element: <SelectionPage />,
    },
    {
        path: '/dashboard',
        element: <DashboardPage />,
    },
]);
