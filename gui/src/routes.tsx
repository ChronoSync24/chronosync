import React from 'react';
import DashboardIcon from '@mui/icons-material/Dashboard';
import ScienceIcon from '@mui/icons-material/Science';
import PeopleIcon from '@mui/icons-material/People';
import EventIcon from '@mui/icons-material/Event';
import BusinessIcon from '@mui/icons-material/Business';
import CalendarTodayIcon from '@mui/icons-material/CalendarToday';
import { type Navigation } from '@toolpad/core/AppProvider';
import HomePage from './pages/HomePage';
import TestsPage from './pages/TestsPage';
import UsersPage from './pages/UsersPage';
import AppointmentTypePage from './pages/AppointmentTypePage';
import ClientsPage from './pages/ClientsPage';
import AppointmentPage from './pages/AppointmentPage';
import { CalendarIcon } from '@mui/x-date-pickers/icons';

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
  },
  { // TODO: Change icon
    path: '/appointment-types',
    title: 'Appointment Types',
    icon: <EventIcon />,
    element: <AppointmentTypePage />,
  },
  {
    path: '/clients',
    title: 'Clients',
    icon: <BusinessIcon />,
    element: <ClientsPage />,
  },
  {
    path: '/appointments',
    title: 'Appointments',
    icon: <CalendarIcon />,
    element: <AppointmentPage />,
  },
];

export const NAVIGATION: Navigation = APP_ROUTES.map((route) => ({
  segment: route.path.substring(1),
  title: route.title,
  icon: route.icon,
}));