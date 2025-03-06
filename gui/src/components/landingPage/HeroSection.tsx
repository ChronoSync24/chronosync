import React from 'react';
import { Box, Typography, Button, Theme } from '@mui/material';

interface HeroSectionProps {
  theme: Theme;
  scrollToPlanSection: () => void;
}

const HeroSection: React.FC<HeroSectionProps> = ({ theme, scrollToPlanSection }) => {
  return (
    <Box
      sx={{
        backgroundColor: theme.palette.background.default,
        paddingX: '200px',
        paddingY: '100px',
      }}>
      <div className='mb-12'>
        <div className='mb-2'>
          <Typography variant='h1' color='primary' fontWeight='bold'>
            Effortless organization
          </Typography>
          <Typography variant='h1' color='secondary' fontWeight='bold'>
            Powerful results
          </Typography>
        </div>
        <Typography variant='h6' color='textSecondary'>
          Our features go beyond the ordinary...
        </Typography>
      </div>

      <Button
        variant='contained'
        color='secondary'
        className='h-10 w-60'
        sx={{ fontWeight: 'bold' }}
        onClick={scrollToPlanSection}>
        Become a partner
      </Button>
    </Box>
  );
};

export default HeroSection;
