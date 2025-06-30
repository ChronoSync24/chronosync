import React from 'react';
import { Box, Typography } from '@mui/material';

const TestsPage: React.FC = () => {
  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" gutterBottom>
        Tests Page
      </Typography>
      <Typography>
        This is a test page to verify the navigation and layout.
      </Typography>
    </Box>
  );
};

export default TestsPage;