import React, { useContext, useState, createContext, ReactNode } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { Box } from "@mui/material";
import { ThemeContext, ThemeProviderWrapper } from "./theme/ThemeContext";
import LandingPage from "./pages/LandingPage";
import LoginPage from "./pages/LoginPage";
import DashboardLayoutBasic from "./components/DashboardLayoutBasic";
import { APP_ROUTES } from "./routes";
import Dialog from "@mui/material/Dialog";

interface OverlayContextType {
  open: (content: ReactNode) => void;
  close: () => void;
}
export const OverlayContext = createContext<OverlayContextType>({
  open: () => {},
  close: () => {},
});

const themeClasses: Record<"light" | "dark", string> = {
  light: "",
  dark: "dark",
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
      <Box className={themeClasses[mode]}>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<LandingPage />} />
            <Route path="/login" element={<LoginPage />} />
            {APP_ROUTES.map((route) => (
              <Route
                key={route.path}
                path={route.path}
                element={
                  <DashboardLayoutBasic>{route.element}</DashboardLayoutBasic>
                }
              />
            ))}
          </Routes>
        </BrowserRouter>
      </Box>
      <Dialog open={overlayOpen} onClose={close} maxWidth="sm" fullWidth>
        {overlayContent}
      </Dialog>
    </OverlayContext.Provider>
  );
};

const App: React.FC = () => (
  <ThemeProviderWrapper>
    <AppContent />
  </ThemeProviderWrapper>
);

export default App;
