import React from 'react';
import PrimaryButton from './PrimaryButton';
import { NavigateFunction, useNavigate } from 'react-router-dom';

/**
 * Service methods.
 */
import { logout } from '../services/AuthService';

const LogoutButton: React.FC = () => {
  const navigate: NavigateFunction = useNavigate();

  const handleLogout = () => {
    if (localStorage.getItem('token')) {
      logout().then(() => {
        localStorage.removeItem('token');
        navigate('/login');
      });
    } else {
      navigate('/login');
    }
  };

  return <PrimaryButton onClick={handleLogout}>Logout</PrimaryButton>;
};

export default LogoutButton;
