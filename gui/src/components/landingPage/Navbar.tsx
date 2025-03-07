import React from 'react';
import { Button, Typography, Box, Theme } from '@mui/material';
import { lighten } from '@mui/system';
import { useNavigate } from 'react-router-dom';

interface NavbarProps {
  theme: Theme;
  scrollToPerksSection: () => void;
  scrollToFeaturesSection: () => void;
  scrollToPlanSection: () => void;
  scrollToContactSection: () => void;
}

const Navbar: React.FC<NavbarProps> = ({
  theme,
  scrollToPerksSection,
  scrollToFeaturesSection,
  scrollToPlanSection,
  scrollToContactSection,
}) => {
  const navigate = useNavigate();

  const buttonStyles = {
    '&:hover': {
      backgroundColor: lighten(theme.palette.secondary.main, 0.6),
    },
  };

  return (
    <Box
      sx={{
        backgroundColor: theme.palette.background.default,
        paddingX: '200px',
        paddingY: '20px',
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        width: '100%',
      }}>
      <Typography color='primary' variant='h6'>
        ChronoSync Logo
      </Typography>
      <Box sx={{ display: 'flex', gap: 2, mx: 'auto' }}>
        <Button sx={buttonStyles} onClick={scrollToPerksSection}>
          Perks
        </Button>
        <Button sx={buttonStyles} onClick={scrollToFeaturesSection}>
          Features
        </Button>
        <Button sx={buttonStyles} onClick={scrollToPlanSection}>
          Plans
        </Button>
        <Button sx={buttonStyles} onClick={scrollToContactSection}>
          Contact
        </Button>
      </Box>
      <Button
        variant='contained'
        color='secondary'
        className='h-10'
        sx={{ fontWeight: 'bold' }}
        onClick={() => navigate('/login')}>
        Login
      </Button>
    </Box>
  );
};

export default Navbar;
