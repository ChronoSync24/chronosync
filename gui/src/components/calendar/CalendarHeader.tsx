import React from 'react';
import {
  Box,
  Typography,
  IconButton,
  Button,
  ButtonGroup,
  useTheme
} from '@mui/material';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import PrimaryButton from '../PrimaryButton';

type ViewType = 'day' | 'week' | 'month';

interface CalendarHeaderProps {
  viewType: ViewType;
  currentDate: Date;
  onViewTypeChange: (view: ViewType) => void;
  onPrevious: () => void;
  onNext: () => void;
  onToday: () => void;
  getDisplayDateRange: () => string;
}

const CalendarHeader: React.FC<CalendarHeaderProps> = ({
  viewType,
  currentDate,
  onViewTypeChange,
  onPrevious,
  onNext,
  onToday,
  getDisplayDateRange,
}) => {
  const theme = useTheme();

  return (
    <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
      <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
        <Typography variant="h4" sx={{ fontWeight: 600, color: theme.palette.text.primary }}>
          Calendar
        </Typography>

        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
          <IconButton onClick={onPrevious} sx={{ color: theme.palette.secondary.main }}>
            <ChevronLeftIcon />
          </IconButton>
          <Typography variant="h6" sx={{ fontWeight: 500, minWidth: 200, textAlign: 'center' }}>
            {getDisplayDateRange()}
          </Typography>
          <IconButton onClick={onNext} sx={{ color: theme.palette.secondary.main }}>
            <ChevronRightIcon />
          </IconButton>
        </Box>
      </Box>

      <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
        <PrimaryButton onClick={onToday} sx={{ minWidth: 80 }}>
          Today
        </PrimaryButton>

        <ButtonGroup variant="outlined" size="small">
          {['day', 'week', 'month'].map((v) => (
            <Button
              key={v}
              variant={viewType === v ? 'contained' : 'outlined'}
              onClick={() => onViewTypeChange(v as ViewType)}
              color="secondary"
              sx={
                viewType === v
                  ? {
                      backgroundColor: theme.palette.secondary.main,
                      color: theme.palette.secondary.contrastText,
                      '&:hover': { backgroundColor: theme.palette.secondary.dark },
                    }
                  : { minWidth: 60 }
              }
            >
              {v.charAt(0).toUpperCase() + v.slice(1)}
            </Button>
          ))}
        </ButtonGroup>
      </Box>
    </Box>
  );
};

export default CalendarHeader;
