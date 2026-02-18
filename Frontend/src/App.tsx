import { RouterProvider } from 'react-router-dom';
import { router } from './routes';
import { StoreProvider } from './context/StoreContext';

function App() {
  return (
    <StoreProvider>
      <RouterProvider router={router} />
    </StoreProvider>
  );
}

export default App;
