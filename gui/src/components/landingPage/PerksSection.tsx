import React, { forwardRef } from 'react';
import { Box, Typography, Card, CardContent, Theme } from '@mui/material';
import BusinessIcon from '@mui/icons-material/Business';
import GroupsIcon from '@mui/icons-material/Groups';
import HandShakeIcon from '@mui/icons-material/HandShake';

interface PerksSectionProps {
  theme: Theme;
}

const PerksSection = forwardRef<HTMLDivElement, PerksSectionProps>(({ theme }, ref) => {
  return (
    <Box
      ref={ref}
      sx={{
        backgroundColor: theme.palette.background.paper,
        paddingY: '96px',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        flexDirection: 'column',
        textAlign: 'center',
      }}>
      <div className='mb-12'>
        <Typography
          variant='h3'
          sx={{
            maxWidth: '700px',
            fontWeight: 'bold',
            marginBottom: '8px',
          }}
          color='primary'>
          Manage your entire business in a single system
        </Typography>
        <Typography variant='h6' color='textSecondary'>
          Who is ChronoSync suitable for?
        </Typography>
      </div>

      <Box
        sx={{
          display: 'flex',
          justifyContent: 'center',
          flexDirection: { xs: 'column', sm: 'row' },
          gap: '180px',
          width: '100%',
        }}>
        <Card
          elevation={2}
          sx={{
            width: '350px',
            padding: '10px',
            borderRadius: '8px',
          }}>
          <CardContent>
            <div className='flex justify-center items-center'>
              <div
                className={`w-20 h-20 p-2 rounded-[16px_8px_16px_8px] mb-6`}
                style={{
                  border: `1px solid ${theme.palette.background.default}`,
                  backgroundColor: theme.palette.background.default,
                }}>
                <GroupsIcon sx={{ fontSize: '3.5rem' }} color='primary' />
              </div>
            </div>
            <div className='mb-6'>
              <Typography variant='h4' fontWeight='bold'>
                Small Organizations
              </Typography>
            </div>
            <Typography variant='body1' color='textSecondary'>
              Perfect for small businesses and startups, our service helps you manage appointments, track employee
              hours, and handle finances effortlessly-all without the complexity of larger systems
            </Typography>
          </CardContent>
        </Card>
        <Card
          elevation={2}
          sx={{
            width: '350px',
            padding: '10px',
            borderRadius: '8px',
          }}>
          <CardContent>
            <div className='flex justify-center items-center'>
              <div
                className={`w-20 h-20 p-2 rounded-[16px_8px_16px_8px] mb-6`}
                style={{
                  border: `1px solid ${theme.palette.background.default}`,
                  backgroundColor: theme.palette.background.default,
                }}>
                <BusinessIcon sx={{ fontSize: '3.5rem' }} color='primary' />
              </div>
            </div>
            <div className='mb-6'>
              <Typography variant='h4' fontWeight='bold'>
                Large
              </Typography>
              <Typography variant='h4' fontWeight='bold'>
                Businesses
              </Typography>
            </div>
            <Typography variant='body1' color='textSecondary'>
              Built to handle the demands of large organizations, our platform offers advanced tools for managing
              complex schedules, detailed financial reports, and in-depth analytics to drive smarter decisions
            </Typography>
          </CardContent>
        </Card>
        <Card
          elevation={2}
          sx={{
            width: '350px',
            padding: '10px',
            borderRadius: '8px',
          }}>
          <CardContent>
            <div className='flex justify-center items-center'>
              <div
                className={`w-20 h-20 p-2 rounded-[16px_8px_16px_8px] mb-6`}
                style={{
                  border: `1px solid ${theme.palette.background.default}`,
                  backgroundColor: theme.palette.background.default,
                }}>
                <HandShakeIcon sx={{ fontSize: '3.5rem' }} color='primary' />
              </div>
            </div>
            <div className='mb-6'>
              <Typography variant='h4' fontWeight='bold'>
                Clubs And Groups
              </Typography>
            </div>
            <Typography variant='body1' color='textSecondary'>
              Whether you are managing a sports team, community group, or nonprofit, our application makes it easy to
              organize schedules, track participation, and manage budgets-all in one place
            </Typography>
          </CardContent>
        </Card>
      </Box>
    </Box>
  );
});
export default PerksSection;
