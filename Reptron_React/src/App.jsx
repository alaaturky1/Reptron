import { createBrowserRouter, RouterProvider, Navigate } from 'react-router-dom'
import '@fortawesome/fontawesome-free/css/all.min.css'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import './App.css'
import Layout from './components/Layout/Layout'
import Login from './components/Login/Login'
import Register from './components/Register/Register'
import NotFound from './components/NotFound/NotFound'
import Home from './components/Home/Home'
import AboutUs from './components/AboutUs/AboutUs'
import Coaches from './components/Coaches/Coaches'
import Equipments from './components/Equipments/Equipments'
import Supplements from './components/Supplements/Supplements'
import EquipmentsDetails from './components/EquipmentsDetails/EquipmentsDetails'
import ProductDetails from './components/ProductDetails/ProductDetails'
import CoachDetails from './components/CoachDetails/CoachDetails'
import UserContextProvider from './context/userContext'
import CoachesProfiles from './components/CoachesProfiles/CoachesProfiles'
import ProtectedRoute from './components/ProtectedRoute/ProtectedRoute'
import Cart from './components/Cart/Cart'
import { CartContextProvider } from './context/cartContext'
import { Toaster } from 'react-hot-toast'
import CheckOut from './components/CheckOut/CheckOut'
import PurchaseContextProvider from './context/purchasesContext'
import Profile from './components/Profile/Profile'
import ScrollToTopButton from './components/ScrollToTopButton/ScrollToTopButton'
import AiCoachLayout from './components/AiCoach/AiCoachLayout'
import AiCoachHome from './components/AiCoach/AiCoachHome'
import AiCoachLive from './components/AiCoach/AiCoachLive'
import AiCoachHistory from './components/AiCoach/AiCoachHistory'
import AiCoachResult from './components/AiCoach/AiCoachResult'
function App() {

  const router = createBrowserRouter([
    {
      path: '', 
      element: <Layout />, 
      children: [
        { index: true, element: <Home/> },
        { path: 'login', element: <Login/> },
        { path: 'register', element: <Register/> },
        { path: 'aboutUs', element:<ProtectedRoute> <AboutUs/> </ProtectedRoute> },
        { path: 'coaches', element:<ProtectedRoute> <Coaches/> </ProtectedRoute>},
        { path: 'coach/:id', element:<ProtectedRoute> <CoachDetails/> </ProtectedRoute>},
        { path: 'coachesProfiles/:id', element:<ProtectedRoute> <CoachesProfiles/> </ProtectedRoute>},
        { path: 'equipments', element: <ProtectedRoute> <Equipments/> </ProtectedRoute>},
        { path: '/store', element: <Navigate to="/supplements" replace /> },
        { path: '/supplements', element:<ProtectedRoute> <Supplements/> </ProtectedRoute>},
        { path: '/equipments/:id', element:<ProtectedRoute> <EquipmentsDetails/> </ProtectedRoute>},
        { path: '/product/:id', element:<ProtectedRoute> <ProductDetails/> </ProtectedRoute>},
        { path: 'cart', element:<ProtectedRoute> <Cart/> </ProtectedRoute>},
        { path: 'checkout', element:<ProtectedRoute> <CheckOut/> </ProtectedRoute>},
        { path: '/profile', element:<ProtectedRoute> <Profile/> </ProtectedRoute>},
        { path: '/mypurchases', element: <Navigate to="/profile" replace /> },
        {
          path: 'ai',
          element: (
            <ProtectedRoute>
              <AiCoachLayout />
            </ProtectedRoute>
          ),
          children: [
            { index: true, element: <AiCoachHome /> },
            { path: 'live', element: <AiCoachLive /> },
            { path: 'history', element: <AiCoachHistory /> },
            { path: 'result', element: <AiCoachResult /> },
          ],
        },
        { path: 'coach', element: <Navigate to="/ai" replace /> },
        { path: '*', element: <NotFound/> },
      ]
    }
  ])

  return (
    <>
      <PurchaseContextProvider>
        <UserContextProvider>
          <CartContextProvider>
            <RouterProvider router={router} />
            <Toaster/>
            <ScrollToTopButton />
          </CartContextProvider>
        </UserContextProvider>
      </PurchaseContextProvider>
    </>
  )
}

export default App