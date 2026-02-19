import { createBrowserRouter } from 'react-router-dom';
import SelectionPage from '../pages/SelectionPage';
import DashboardPage from '../pages/DashboardPage';
import OrderDetailsPage from '../pages/OrderDetailsPage';

export const router = createBrowserRouter([
    {
        path: '/',
        element: <SelectionPage />,
    },
    {
        path: '/dashboard',
        element: <DashboardPage />,
    },
    {
        path: '/order/:id',
        element: <OrderDetailsPage />,
    }
]);
