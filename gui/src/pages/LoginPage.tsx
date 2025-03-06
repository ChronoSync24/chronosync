import React from 'react';
import { Typography, Box } from '@mui/material';

const LoginPage: React.FC = () => {
  return (
    <Box
      sx={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        height: '100vh',
      }}>
      <Typography variant='h3'>Login Page</Typography>
    </Box>
  );
};

export default LoginPage;
