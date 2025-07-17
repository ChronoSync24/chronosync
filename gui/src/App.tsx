import React, { useContext, useState, createContext, ReactNode } from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { Box } from '@mui/material';
import { ThemeContext, ThemeProviderWrapper } from './theme/ThemeContext';
import LandingPage from './pages/LandingPage';
import LoginPage from './pages/LoginPage';
import DashboardLayoutBasic from './components/DashboardLayoutBasic';
import { APP_ROUTES } from './routes';
import FormOverlay from './components/forms/FormOverlay';

// Overlay context
interface OverlayContextType {
  open: (content: ReactNode) => void;
  close: () => void;
}
export const OverlayContext = createContext<OverlayContextType>({ open: () => {}, close: () => {} });

const themeClasses: Record<'light' | 'dark', string> = {
  light: '',
  dark: 'dark',
};

const AppContent: React.FC = () => {
  const { mode } = useContext(ThemeContext);
  const [overlayOpen, setOverlayOpen] = useState(false);
  const [overlayContent, setOverlayContent] = useState<ReactNode>(null);

  const open = (content: ReactNode) => {
    setOverlayContent(content);
    setOverlayOpen(true);
  };
  const close = () => {
    setOverlayOpen(false);
    setOverlayContent(null);
  };

  return (
    <OverlayContext.Provider value={{ open, close }}>
      <Box className={themeClasses[mode]} style={overlayOpen ? { filter: 'blur(4px)', transition: 'filter 0.2s' } : {}}>
        <BrowserRouter>
          <Routes>
            <Route path='/' element={<LandingPage />} />
            <Route path='/login' element={<LoginPage />} />
            {APP_ROUTES.map((route) => (
              <Route
                key={route.path}
                path={route.path}
                element={
                  <DashboardLayoutBasic>
                    {route.element}
                  </DashboardLayoutBasic>
                }
              />
            ))}
          </Routes>
        </BrowserRouter>
      </Box>
      <FormOverlay open={overlayOpen} onClose={close}>
        {overlayContent}
      </FormOverlay>
    </OverlayContext.Provider>
  );
};

const App: React.FC = () => (
  <ThemeProviderWrapper>
    <AppContent />
  </ThemeProviderWrapper>
);

export default App;
