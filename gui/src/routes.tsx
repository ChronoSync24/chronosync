import React from 'react';
import DashboardIcon from '@mui/icons-material/Dashboard';
import ScienceIcon from '@mui/icons-material/Science';
import PeopleIcon from '@mui/icons-material/People';
import { type Navigation } from '@toolpad/core/AppProvider';
import HomePage from './pages/HomePage';
import TestsPage from './pages/TestsPage';
import UsersPage from './pages/UsersPage';

export interface AppRoute {
  path: string;
  title: string;
  icon: React.ReactNode;
  element: React.ReactNode;
}

export const APP_ROUTES: AppRoute[] = [
  {
    path: '/home',
    title: 'Home',
    icon: <DashboardIcon/>,
    element: <HomePage/>
  },
  {
    path: '/tests',
    title: 'Tests',
    icon: <ScienceIcon />,
    element: <TestsPage />,
  },
  {
    path: '/users',
    title: 'Users',
    icon: <PeopleIcon />,
    element: <UsersPage />,
  }
];

export const NAVIGATION: Navigation = APP_ROUTES.map((route) => ({
  segment: route.path.substring(1),
  title: route.title,
  icon: route.icon,
}));