import React from 'react';
import { Box, Typography, Theme } from '@mui/material';
import PrimaryButton from '../PrimaryButton';

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

      <PrimaryButton className='w-60' onClick={scrollToPlanSection}>
        Become a partner
      </PrimaryButton>
    </Box>
  );
};

export default HeroSection;
