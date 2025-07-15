import * as React from 'react';
import { AppProvider } from '@toolpad/core/AppProvider';
import { DashboardLayout } from '@toolpad/core/DashboardLayout';
import {
  useTheme,
  Theme,
  IconButton,
  Box,
} from '@mui/material';
import { useNavigate, useLocation } from 'react-router-dom';
import { NAVIGATION } from '../routes';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';

interface DashboardLayoutBasicProps {
  children: React.ReactNode;
}

export default function DashboardLayoutBasic({ children }: DashboardLayoutBasicProps) {
  const theme: Theme = useTheme();
  const navigate = useNavigate();
  const location = useLocation();

  // Create a router object that integrates with React Router
  const router = React.useMemo(() => ({
    pathname: location.pathname,
    searchParams: new URLSearchParams(location.search),
    navigate: (url: string | URL) => {
      const path = typeof url === 'string' ? url : url.pathname;
      navigate(path);
    },
  }), [location, navigate]);

  return (
    <AppProvider
      navigation={NAVIGATION}
      theme={theme}
      router={router}
      branding={{
        logo: <img alt="" />,
        title: 'ChronoSync',
        homeUrl: '/home'
      }}
    >
      <DashboardLayout
        disableCollapsibleSidebar
        slots={{
          toolbarActions: () => (
            <Box>
              <IconButton
                sx={{
                  borderRadius: '8px',
                  color: theme.palette.text.primary,
                  '&:hover': {
                    backgroundColor: theme.palette.action.hover,
                  },
                  marginRight: '8px',
                }}
              >
                <AccountCircleIcon />
              </IconButton>
            </Box>
          ),
        }}
        sx={{
          backgroundColor: theme.palette.background.paper,
          
          '& .MuiDrawer-paper': {
            backgroundColor: theme.palette.background.default,
            borderRight: '1px solid #b3b3b3',
          },

          '& .MuiToolbar-root': {
            backgroundColor: theme.palette.background.default,
            borderBottom: '1px solid #b3b3b3',
          },

          '& .MuiList-root': {
            '& .MuiListItemButton-root': {
              borderRadius: '8px',
              margin: '4px 8px',

              '&:hover': {
                backgroundColor: theme.palette.navigation.hover.background,
              },

              '&.Mui-selected': {
                backgroundColor: theme.palette.navigation.active.background,
                color: theme.palette.navigation.active.color,

                '&:hover': {
                  backgroundColor: theme.palette.navigation.active.background,
                },

                '& .MuiListItemIcon-root': {
                  color: theme.palette.navigation.active.color,
                },

                '& .MuiListItemText-root .MuiTypography-root': {
                  color: theme.palette.navigation.active.color,
                  fontWeight: 600,
                },
              },
            },
          },
        }}
      >
        {children}
      </DashboardLayout>
    </AppProvider>
  );
}