import { createTheme, ThemeOptions } from '@mui/material/styles';

// Extend the Material-UI theme interface to include custom navigation colors
declare module '@mui/material/styles' {
  interface Palette {
    navigation: {
      active: {
        background: string;
        color: string;
      };
      hover: {
        background: string;
      };
    };
  }

  interface PaletteOptions {
    navigation?: {
      active?: {
        background?: string;
        color?: string;
      };
      hover?: {
        background?: string;
      };
    };
  }
}

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
      navigation: {
        active: {
          background: '#D2CEF0',
          color: 'black',
        },
        hover: {
          background: 'rgba(210, 206, 240, 0.6)',
        },
      },
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
      navigation: {
        active: {
          background: '#D2CEF0',
          color: '#5549A4',
        },
        hover: {
          background: 'rgba(210, 206, 240, 0.4)',
        },
      },
    },
  },
};

export const getTheme = (mode: keyof typeof themes) => createTheme(themes[mode]);
