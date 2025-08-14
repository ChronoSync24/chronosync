import React, { useContext } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Box } from '@mui/material';
import { ThemeContext, ThemeProviderWrapper } from './theme/ThemeContext';
import LandingPage from './pages/LandingPage';
import LoginPage from './pages/LoginPage';
import DashboardLayoutBasic from './components/DashboardLayoutBasic';
import { APP_ROUTES } from './routes';
import ProtectedRoute from './components/ProtectedRoute';

const themeClasses: Record<'light' | 'dark', string> = {
  light: '',
  dark: 'dark',
};

const AppContent: React.FC = () => {
  const { mode } = useContext(ThemeContext);
  return (
    <Box className={themeClasses[mode]}>
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<LandingPage />} />
          <Route path='/login' element={<LoginPage />} />
          {APP_ROUTES.map((route) => {
            const page = (
              <DashboardLayoutBasic>{route.element}</DashboardLayoutBasic>
            );
            return (
              <Route
                key={route.path}
                path={route.path}
                element={
                  route.minRole ?
                    <ProtectedRoute minRole={route.minRole}>
                      {page}
                    </ProtectedRoute>
                  : page
                }
              />
            );
          })}
        </Routes>
      </BrowserRouter>
    </Box>
  );
};

const App: React.FC = () => (
  <ThemeProviderWrapper>
    <AppContent />
  </ThemeProviderWrapper>
);

export default App;
