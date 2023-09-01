import {
  createBrowserRouter,
  RouterProvider
} from 'react-router-dom'
import Home from './pages/Home';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import Signup from './pages/Signup';
import PasteDetail from './pages/PasteDetail';
import { Container } from '@mui/material';

function App() {
  const router = createBrowserRouter([
    {
      path: "/",
      element: <Home />
    },
    {
      path: "/paste/:id",
      element: <PasteDetail />
    },
    {
      path: "/login",
      element: <Login />
    },
    {
      path: "/signup",
      element: <Signup />
    },
    {
      path: "/dashboard",
      element: <Dashboard />
    },
  ]);

  return (
    <Container>
      <RouterProvider router={router} />
    </Container>
  );
}

export default App;
