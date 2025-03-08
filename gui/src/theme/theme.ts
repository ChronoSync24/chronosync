import { createTheme, ThemeOptions } from '@mui/material/styles';

const themes: Record<'light' | 'dark', ThemeOptions> = {
  light: {
    typography: {
      fontFamily: 'Poppins',
    },
    palette: {
      mode: 'light',
      primary: { main: '#262838' },
      secondary: { main: '#6A5BCD' },
      background: { default: '#E6E6FA', paper: '#FFFFFF' },
      text: { primary: '#262838', secondary: '#717171' },
      info: { main: '#2194f3' },
    },
  },
  dark: {
    palette: {
      mode: 'dark',
      primary: { main: '#1E293B' },
      secondary: { main: '#64748B' },
      background: { default: '#1E293B', paper: '#111827' },
      text: { primary: '#262838', secondary: '#E5E7EB' },
      info: { main: '#2194f3' },
    },
  },
};

export const getTheme = (mode: keyof typeof themes) => createTheme(themes[mode]);
