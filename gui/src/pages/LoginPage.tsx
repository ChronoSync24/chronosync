import React, { useState, useEffect } from 'react';
import {
  Card,
  Typography,
  Box,
  useTheme,
  Theme,
  FormControl,
  InputLabel,
  OutlinedInput,
  InputAdornment,
  IconButton,
  FormHelperText,
  Alert,
} from '@mui/material';
import { Visibility, VisibilityOff } from '@mui/icons-material';
import { Link, NavigateFunction, useNavigate } from 'react-router-dom';
import PrimaryButton from '../components/PrimaryButton';

/**
 * Service methods.
 */
import { login, validateToken } from '../services/AuthService';

/**
 * Data transfer objects.
 */
import { LoginRequestDTO } from '../dtos/requests/LoginRequestDTO';

const LoginPage: React.FC = () => {
  const theme: Theme = useTheme();
  const navigate: NavigateFunction = useNavigate();

  const [showPassword, setShowPassword] = useState(false);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [isSubmitted, setIsSubmitted] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);

  useEffect(() => {
    if (localStorage.getItem('token')) {
      validateToken().then(response => {
        if (response) {
          navigate('/home');
        }
      });
    }
  }, []);

  const handleClickShowPassword = () => setShowPassword(show => !show);

  const handleLogin = (event: React.FormEvent) => {
    event.preventDefault();
    setIsSubmitted(true);

    const request: LoginRequestDTO = { username, password };

    if (request.username.trim() !== '' && request.password.trim() !== '') {
      login(request)
        .then(jwtString => {
          localStorage.setItem('token', jwtString);
          navigate('/home');
        })
        .catch(error => {
          setErrorMessage(error.message);
        });
    }
  };

  return (
    <Box
      sx={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        background: theme.palette.background.default,
        paddingX: '64px',
        paddingY: '64px',
        height: '100vh',
      }}>
      <Card
        sx={{
          background: theme.palette.background.paper,
          paddingX: '64px',
          paddingY: '48px',
          borderRadius: '8px',
        }}>
        <div className='flex flex-col justify-center items-center mb-8'>
          <Typography variant='h4' fontWeight='bold'>
            Login to Your Account
          </Typography>
          <div className='flex'>
            <Typography marginRight='6px' variant='body1' color='textSecondary'>
              You don't have an account?
            </Typography>
            <Typography
              color={theme.palette.info.main}
              className='hover:underline block'
              marginBottom='8px'
              component={Link}
              to='/contact'>
              Contact Us
            </Typography>
          </div>
        </div>

        {errorMessage && (
          <Alert severity='error' sx={{ width: '100%', marginBottom: '16px' }}>
            {errorMessage}
          </Alert>
        )}

        <form
          onSubmit={handleLogin}
          style={{
            width: '400px',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            flexDirection: 'column',
          }}>
          <FormControl sx={{ width: '100%', marginBottom: '16px' }} variant='outlined'>
            <InputLabel htmlFor='username'>Username</InputLabel>
            <OutlinedInput
              id='username'
              label='Username'
              sx={{ borderRadius: '8px' }}
              onChange={e => setUsername(e.target.value)}
            />
            {isSubmitted && username.trim() === '' && (
              <FormHelperText sx={{ color: 'error.main' }}>Username is required</FormHelperText>
            )}
          </FormControl>
          <FormControl sx={{ width: '100%' }} variant='outlined'>
            <InputLabel htmlFor='password'>Password</InputLabel>
            <OutlinedInput
              id='password'
              type={showPassword ? 'text' : 'password'}
              sx={{ borderRadius: '8px' }}
              onChange={e => setPassword(e.target.value)}
              endAdornment={
                <InputAdornment position='end'>
                  <IconButton onClick={handleClickShowPassword} edge='end'>
                    {showPassword ? <VisibilityOff /> : <Visibility />}
                  </IconButton>
                </InputAdornment>
              }
              label='Password'
            />
            {isSubmitted && password.trim() === '' && (
              <FormHelperText sx={{ color: 'error.main' }}>Password is required</FormHelperText>
            )}
          </FormControl>
          <Typography
            sx={{ alignSelf: 'flex-end', marginBottom: '32px' }}
            color='textSecondary'
            className='hover:underline'
            component={Link}
            to='/reset-password'>
            Forgot Password?
          </Typography>
          <PrimaryButton className='w-50' type='submit'>
            Login
          </PrimaryButton>
        </form>
      </Card>
    </Box>
  );
};

export default LoginPage;
