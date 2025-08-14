import React from 'react';
import DashboardIcon from '@mui/icons-material/Dashboard';
import ScienceIcon from '@mui/icons-material/Science';
import PeopleIcon from '@mui/icons-material/People';
import EventIcon from '@mui/icons-material/Event';
import BusinessIcon from '@mui/icons-material/Business';
import { type Navigation } from '@toolpad/core/AppProvider';
import HomePage from './pages/HomePage';
import TestsPage from './pages/TestsPage';
import UsersPage from './pages/UsersPage';
import AppointmentTypePage from './pages/AppointmentTypePage';
import ClientsPage from './pages/ClientsPage';
import { UserRole } from './models/user/UserRole';

export interface AppRoute {
  path: string;
  title: string;
  icon: React.ReactNode;
  element: React.ReactNode;
  minRole?: UserRole;
}

export const APP_ROUTES: AppRoute[] = [
  {
    path: '/home',
    title: 'Home',
    icon: <DashboardIcon />,
    element: <HomePage />,
    minRole: UserRole.EMPLOYEE,
  },
  {
    path: '/tests',
    title: 'Tests',
    icon: <ScienceIcon />,
    element: <TestsPage />,
    minRole: UserRole.ADMIN,
  },
  {
    path: '/users',
    title: 'Users',
    icon: <PeopleIcon />,
    element: <UsersPage />,
    minRole: UserRole.MANAGER,
  },
  {
    path: '/appointment-types',
    title: 'Appointment Types',
    icon: <EventIcon />,
    element: <AppointmentTypePage />,
    minRole: UserRole.MANAGER,
  },
  {
    path: '/clients',
    title: 'Clients',
    icon: <BusinessIcon />,
    element: <ClientsPage />,
    minRole: UserRole.EMPLOYEE,
  },
];

export const NAVIGATION: Navigation = APP_ROUTES.map((route) => ({
  segment: route.path.substring(1),
  title: route.title,
  icon: route.icon,
}));
