/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        primary: '#4CAF4F',
        secondary: '#263238',
        info: '#2194f3',
        shade_1: '#43A046',
        shade_2: '#388E3B',
        shade_3: '#237D31',
        shade_4: '#1B5E1F',
        shade_5: '#103E13',
        tint_1: '#66BB69',
        tint_2: '#81C784',
        tint_3: '#A5D6A7',
        tint_4: '#C8E6C9',
        tint_5: '#E8F5E9',
        warning: '#FBC02D',
        error: '#E53835',
        success: '#2E7D31',
        heading_black: '#4D4D4D',
        text_black: '#717171',
        bg_gray: '#F5F7FA',
      },
    },
  },
  plugins: [],
};
