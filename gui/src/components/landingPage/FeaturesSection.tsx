import React, { forwardRef } from 'react';
import { Box, Typography, Theme } from '@mui/material';
import EventIcon from '@mui/icons-material/Event';
import AccessTimeIcon from '@mui/icons-material/AccessTime';
import AnalyticsIcon from '@mui/icons-material/Analytics';
import AttachMoneyIcon from '@mui/icons-material/AttachMoney';

interface FeaturesSectionProps {
  theme: Theme;
}

const FeaturesSection = forwardRef<HTMLDivElement, FeaturesSectionProps>(({ theme }, ref) => {
  return (
    <Box
      ref={ref}
      sx={{
        backgroundColor: theme.palette.background.default,
        paddingX: '200px',
        paddingY: '100px',
      }}
      className='flex justify-between items-start gap-10'>
      <div className='flex-1 self-center mb-12'>
        <div className='mb-6'>
          <Typography variant='h3' color='primary' fontWeight='bold'>
            More than a business
          </Typography>
          <Typography variant='h3' color='secondary' fontWeight='bold'>
            management software
          </Typography>
        </div>
        <Typography variant='h6' color='textSecondary' width={'600px'}>
          Our features go beyond the ordinary, offering tailored solutions to simplify complex tasks and elevate your
          organization’s efficiency
        </Typography>
      </div>
      <div className='flex-1 grid grid-cols-1 md:grid-cols-2 gap-8 place-items-end'>
        <Box width={'300px'} className='flex items-center gap-6'>
          <EventIcon sx={{ fontSize: '3.5rem' }} color='secondary' />
          <div>
            <div className='mb-2'>
              <Typography variant='h4' fontWeight='bold'>
                Appointment Management
              </Typography>
            </div>
            <Typography variant='body1' color='textSecondary'>
              Easily manage and organize appointments
            </Typography>
          </div>
        </Box>
        <Box width={'300px'} className='flex items-center gap-6'>
          <AccessTimeIcon sx={{ fontSize: '3.5rem' }} color='secondary' />
          <div>
            <div className='mb-2'>
              <Typography variant='h4' fontWeight='bold'>
                Time Tracking
              </Typography>
            </div>
            <Typography variant='body1' color='textSecondary'>
              Monitor employee working hours
            </Typography>
          </div>
        </Box>
        <Box width={'300px'} className='flex items-center gap-6'>
          <AnalyticsIcon sx={{ fontSize: '3.5rem' }} color='secondary' />
          <div>
            <div className='mb-2'>
              <Typography variant='h4' fontWeight='bold'>
                Advanced Analytics
              </Typography>
            </div>
            <Typography variant='body1' color='textSecondary'>
              Gain insights with detailed statistics
            </Typography>
          </div>
        </Box>
        <Box width={'300px'} className='flex items-center gap-6'>
          <AttachMoneyIcon sx={{ fontSize: '3.5rem' }} color='secondary' />
          <div>
            <div className='mb-2'>
              <Typography variant='h4' fontWeight='bold'>
                Financial Tools
              </Typography>
            </div>
            <Typography variant='body1' color='textSecondary'>
              Track costs and generate reports
            </Typography>
          </div>
        </Box>
      </div>
    </Box>
  );
});

export default FeaturesSection;
