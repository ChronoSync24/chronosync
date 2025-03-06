import React, { forwardRef } from 'react';
import { Box, Typography, Theme, TextField, InputAdornment, IconButton } from '@mui/material';
import InstagramIcon from '@mui/icons-material/Instagram';
import XIcon from '@mui/icons-material/X';
import FacebookIcon from '@mui/icons-material/Facebook';
import MailOutlineIcon from '@mui/icons-material/MailOutline';
import { Link } from 'react-router-dom';

interface FooterProps {
  theme: Theme;
}

const Footer = forwardRef<HTMLDivElement, FooterProps>(({ theme }, ref) => {
  return (
    <Box
      ref={ref}
      sx={{
        backgroundColor: theme.palette.primary.main,
        paddingX: '200px',
        paddingY: '100px',
        display: 'flex',
        justifyContent: 'space-between',
        color: theme.palette.background.paper,
      }}>
      <div>
        <Typography variant='h4' fontWeight='bold' marginBottom='32px'>
          ChronoSync Logo
        </Typography>
        <Typography fontWeight='light'>Copyright © 2025 ChronoSync</Typography>
        <Typography fontWeight='light' marginBottom='32px'>
          All rights reserved
        </Typography>
        <div className='flex gap-3'>
          <InstagramIcon sx={{ fontSize: '1.5rem' }} />
          <XIcon sx={{ fontSize: '1.5rem' }} />
          <FacebookIcon sx={{ fontSize: '1.5rem' }} />
        </div>
      </div>
      <div>
        <Typography marginBottom='32px' variant='h5' fontWeight='bold'>
          Company
        </Typography>
        <Typography className='hover:underline block' marginBottom='8px' component={Link} to='/about'>
          About Us
        </Typography>
        <Typography className='hover:underline block' marginBottom='8px' component={Link} to='/contact'>
          Contact Us
        </Typography>
        <Typography className='hover:underline block' marginBottom='8px' component={Link} to='/pricing'>
          Pricing
        </Typography>
      </div>
      <div>
        <Typography marginBottom='32px' variant='h5' fontWeight='bold'>
          Support
        </Typography>
        <Typography className='hover:underline block' marginBottom='8px' component={Link} to='/help'>
          Help center
        </Typography>
        <Typography className='hover:underline block' marginBottom='8px' component={Link} to='/terms'>
          Terms of Service
        </Typography>
        <Typography className='hover:underline block' marginBottom='8px' component={Link} to='/legal'>
          Legal
        </Typography>
        <Typography className='hover:underline block' marginBottom='8px' component={Link} to='/privacy'>
          Privacy Policy
        </Typography>
        <Typography className='hover:underline block' marginBottom='8px' component={Link} to='/status'>
          Status
        </Typography>
      </div>
      <div>
        <Typography marginBottom='32px' variant='h5' fontWeight='bold'>
          Stay up to date
        </Typography>
        <TextField
          fullWidth
          placeholder='Your email address'
          type='email'
          variant='outlined'
          sx={{
            borderRadius: '8px',
            backgroundColor: 'white',
            width: '100%',
          }}
          slotProps={{
            input: {
              endAdornment: (
                <InputAdornment position='end'>
                  <IconButton onClick={() => alert('Subscribed!')}>
                    <MailOutlineIcon />
                  </IconButton>
                </InputAdornment>
              ),
            },
          }}
        />
      </div>
    </Box>
  );
});

export default Footer;
