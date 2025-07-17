import React from 'react';
import { Button, ButtonProps } from '@mui/material';

interface SecondaryButtonProps extends ButtonProps {
  className?: string;
}

const SecondaryButton: React.FC<SecondaryButtonProps> = ({ children, sx, className, ...props }) => {
  return (
    <Button
      className={className}
      variant='contained'
      color='primary'
      sx={{
        paddingY: '8px',
        paddingX: '12px',
        backgroundColor: '#fff',
        color: '#6A5BCD',
        '&:hover': {
          backgroundColor: '#4A3F8F',
          color: '#fff',
        },
        ...sx,
      }}
      {...props}
    >
      {children}
    </Button>
  );
};

export default SecondaryButton;
