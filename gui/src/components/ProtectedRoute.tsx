import React from 'react';
import { Navigate } from 'react-router-dom';
import { UserRole } from '../models/user/UserRole';
import { hasAtLeastRole, isTokenExpired } from '../utils/tokenUtils';

type ProtectedRouteProps = {
  minRole: UserRole;
  children: React.ReactElement;
  redirectTo?: string;
};

export default function ProtectedRoute({
  minRole,
  children,
  redirectTo = '/login',
}: ProtectedRouteProps) {
  const token = localStorage.getItem('token') || '';

  if (!token || isTokenExpired(token)) {
    return <Navigate to={redirectTo} replace />;
  }

  if (!hasAtLeastRole(token, minRole)) {
    return <Navigate to='/home' replace />;
  }

  return children;
}
