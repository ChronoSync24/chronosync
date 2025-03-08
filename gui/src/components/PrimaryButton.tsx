import React from 'react';
import { Button, ButtonProps } from '@mui/material';

interface PrimaryButtonProps extends ButtonProps {
  className?: string;
}

const PrimaryButton: React.FC<PrimaryButtonProps> = ({ children, sx, className, ...props }) => {
  return (
    <Button
      className={className}
      variant='contained'
      color='secondary'
      sx={{
        paddingY: '8px',
        paddingX: '12px',
        ...sx,
      }}
      {...props}>
      {children}
    </Button>
  );
};

export default PrimaryButton;
