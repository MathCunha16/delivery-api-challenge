import { createBrowserRouter } from 'react-router-dom';
import SelectionPage from '../pages/SelectionPage';

export const router = createBrowserRouter([
    {
        path: '/',
        element: <SelectionPage />,
    },
    {
        path: '/dashboard',
        element: <div>Dashboard Placeholder</div>,
    },
]);
