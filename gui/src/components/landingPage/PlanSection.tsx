import React, { forwardRef } from 'react';
import { Box, Typography, Card, CardContent, Theme } from '@mui/material';
import LayersIcon from '@mui/icons-material/Layers';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import RocketLaunchIcon from '@mui/icons-material/RocketLaunch';
import PrimaryButton from '../PrimaryButton';

interface PlanSectionProps {
  theme: Theme;
}

const PlanSection = forwardRef<HTMLDivElement, PlanSectionProps>(({ theme }, ref) => {
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
          Pick the perfect plan for your business
        </Typography>
        <div className='flex gap-3 justify-center'>
          <Typography variant='h6' color='textSecondary'>
            Billed Monthly
          </Typography>
          <Typography variant='h6' color='textSecondary'>
            |
          </Typography>
          <Typography variant='h6' color='secondary' fontWeight='bold'>
            Billed yearly
          </Typography>
          <Box
            sx={{
              border: `1px solid ${theme.palette.background.default}`,
              borderRadius: '10px',
              paddingX: '2px',
              backgroundColor: theme.palette.background.default,
            }}>
            <Typography variant='h6' color='secondary'>
              Save up to 20%
            </Typography>
          </Box>
        </div>
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
                <LayersIcon sx={{ fontSize: '3.5rem' }} color='primary' />
              </div>
            </div>
            <div className='mb-6'>
              <Typography variant='h4' fontWeight='bold'>
                Basic
              </Typography>
            </div>
            <div className='mb-6'>
              <Typography variant='body1' color='textSecondary'>
                Simplify your core tasks with essential tools for appointment management, time tracking, and financial
                oversight
              </Typography>
            </div>
            <div className='flex items-end gap-1 mb-4'>
              <Typography variant='h4' fontWeight='bold' sx={{ lineHeight: 1 }}>
                $10
              </Typography>
              <Typography variant='body2' className='underline decoration-dotted'>
                /seat/mo
              </Typography>
            </div>
            <PrimaryButton
              sx={{
                width: '100%',
              }}>
              Get started
            </PrimaryButton>
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
                <TrendingUpIcon sx={{ fontSize: '3.5rem' }} color='primary' />
              </div>
            </div>
            <div className='mb-6'>
              <Typography variant='h4' fontWeight='bold'>
                Advanced
              </Typography>
            </div>
            <div className='mb-6'>
              <Typography variant='body1' color='textSecondary'>
                Enhance your workflow with advanced scheduling, detailed reporting, and productivity-boosting features
              </Typography>
            </div>
            <div className='flex items-end gap-1 mb-4'>
              <Typography variant='h4' fontWeight='bold' sx={{ lineHeight: 1 }}>
                $15
              </Typography>
              <Typography variant='body2' className='underline decoration-dotted'>
                /seat/mo
              </Typography>
            </div>
            <PrimaryButton
              sx={{
                width: '100%',
              }}>
              Get started
            </PrimaryButton>
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
                <RocketLaunchIcon sx={{ fontSize: '3.5rem' }} color='primary' />
              </div>
            </div>
            <div className='mb-6'>
              <Typography variant='h4' fontWeight='bold'>
                Intermediate
              </Typography>
            </div>
            <div className='mb-6'></div>
            <div className='mb-6'>
              <Typography variant='body1' color='textSecondary'>
                Empower your organization with premium tools, in-depth analytics, and priority support for seamless
                operations.
              </Typography>
            </div>
            <div className='flex items-end gap-1 mb-4'>
              <Typography variant='h4' fontWeight='bold' sx={{ lineHeight: 1 }}>
                $20
              </Typography>
              <Typography variant='body2' className='underline decoration-dotted'>
                /seat/mo
              </Typography>
            </div>
            <PrimaryButton
              sx={{
                width: '100%',
              }}>
              Get started
            </PrimaryButton>
          </CardContent>
        </Card>
      </Box>
    </Box>
  );
});

export default PlanSection;
