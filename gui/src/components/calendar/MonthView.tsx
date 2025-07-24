import React from 'react';
import { Box, Paper, Typography, useTheme } from '@mui/material';
import { Appointment } from '../../models/Appointment';
import { getAppointmentDotsForDay } from './utils';

interface MonthViewProps {
  daysOfMonth: (Date | null)[];
  appointments: Appointment[];
  onDaySelect: (date: Date) => void;
}

const MonthView: React.FC<MonthViewProps> = ({ daysOfMonth, appointments, onDaySelect }) => {
  const theme = useTheme();

  return (
    <Paper elevation={2} sx={{ height: 'calc(100vh - 140px)', overflow: 'hidden', borderRadius: 2 }}>
      <Box sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
        <Box sx={{ display: 'flex', minHeight: 60, borderBottom: `1px solid ${theme.palette.divider}`, backgroundColor: theme.palette.background.paper }}>
          {["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"].map((label, index) => (
            <Box
              key={label}
              sx={{ flex: 1, borderRight: index < 6 ? `1px solid ${theme.palette.divider}` : 'none', display: 'flex', alignItems: 'center', justifyContent: 'center' }}
            >
              <Typography variant="body2" sx={{ fontWeight: 600, color: theme.palette.text.primary }}>
                {label}
              </Typography>
            </Box>
          ))}
        </Box>

        <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(7, 1fr)', flex: 1, overflow: 'auto' }}>
          {daysOfMonth.map((day, index) => {
            const dots = day ? getAppointmentDotsForDay(appointments, day) : [];
            return (
              <Box
                key={day ? day.toISOString() : `empty-${index}`}
                sx={{
                  minHeight: 120,
                  borderRight: (index + 1) % 7 !== 0 ? `1px solid ${theme.palette.divider}` : 'none',
                  borderBottom: `1px solid ${theme.palette.divider}`,
                  backgroundColor: day ? theme.palette.background.paper : theme.palette.background.default,
                  p: 1,
                  cursor: day ? 'pointer' : 'default',
                  transition: 'all 0.2s ease-in-out',
                  '&:hover': day ? {
                    backgroundColor: theme.palette.action.hover,
                    borderColor: theme.palette.secondary.main,
                    borderWidth: '2px',
                    borderStyle: 'solid'
                  } : {},
                  '&:active': day ? {
                    backgroundColor: theme.palette.action.selected,
                    transform: 'scale(0.98)'
                  } : {}
                }}
                onClick={() => day && onDaySelect(day)}
              >
                {day && (
                  <>
                    <Typography variant="body2" sx={{ fontWeight: 500, color: theme.palette.text.primary, mb: 1 }}>
                      {day.getDate()}
                    </Typography>
                    <Box sx={{ display: 'flex', gap: 0.5, mt: 0.5 }}>
                      {dots.map((color, i) => (
                        <Box key={color + i} sx={{ width: 14, height: 14, borderRadius: '50%', backgroundColor: color }} />
                      ))}
                    </Box>
                  </>
                )}
              </Box>
            );
          })}
        </Box>
      </Box>
    </Paper>
  );
};

export default MonthView;