import React from 'react';
import { Box, Paper, Typography, useTheme } from '@mui/material';
import { Appointment } from '../../models/Appointment';
import { getSlotAppointments } from './utils';

interface DayViewProps {
  date: Date;
  timeSlots: string[];
  appointments: Appointment[];
  onSlotClick: (e: React.MouseEvent, appointment: Appointment | null) => void;
  formatTime: (time: string) => string;
}

const DayView: React.FC<DayViewProps> = ({ date, timeSlots, appointments, onSlotClick, formatTime }) => {
  const theme = useTheme();

  return (
    <Paper elevation={2} sx={{ height: 'calc(100vh - 140px)', overflow: 'hidden', borderRadius: 2 }}>
      <Box sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
        <Box sx={{ display: 'flex', minHeight: 60, borderBottom: `1px solid ${theme.palette.divider}` }}>
          <Box sx={{ width: '12.5%', borderRight: `1px solid ${theme.palette.divider}`, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <Typography variant="body2" sx={{ fontWeight: 500, color: theme.palette.text.secondary }}>
              Time
            </Typography>
          </Box>
          <Box sx={{ flex: 1, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <Typography variant="h6" sx={{ fontWeight: 600 }}>
              {date.toLocaleDateString('en-US', {
                weekday: 'long',
                year: 'numeric',
                month: 'long',
                day: 'numeric',
              })}
            </Typography>
          </Box>
        </Box>

        <Box sx={{ display: 'flex', flex: 1 }}>
          <Box sx={{ width: '12.5%', borderRight: `1px solid ${theme.palette.divider}`, backgroundColor: theme.palette.background.paper, overflowY: 'auto', maxHeight: '100%' }}>
            {timeSlots.map((time, index) => (
              <Box
                key={time}
                sx={{ height: 60, borderBottom: index < timeSlots.length - 1 ? `1px solid ${theme.palette.divider}` : 'none', display: 'flex', alignItems: 'center', justifyContent: 'center' }}
              >
                <Typography variant="caption" sx={{ fontWeight: 500, color: theme.palette.text.secondary }}>
                  {formatTime(time)}
                </Typography>
              </Box>
            ))}
          </Box>

          <Box sx={{ flex: 1, backgroundColor: theme.palette.background.default, overflowY: 'auto', maxHeight: '100%' }}>
            {timeSlots.map((time, index) => {
              const slotAppointments = React.useMemo(() => getSlotAppointments({
                time,
                date,
                appointments,
              }), [time, date, appointments]);
              const firstAppointment = slotAppointments[0] || null;

              return (
                <Box
                  key={time}
                  sx={{
                    height: 60,
                    borderBottom: index < timeSlots.length - 1 ? `1px solid ${theme.palette.divider}` : 'none',
                    borderLeft: `1px solid ${theme.palette.divider}`,
                    backgroundColor: theme.palette.background.paper,
                    cursor: 'pointer',
                    position: 'relative',
                    transition: 'all 0.2s ease-in-out',
                    '&:hover': {
                      backgroundColor: theme.palette.action.hover,
                      borderColor: theme.palette.secondary.main,
                      borderWidth: '2px',
                      borderStyle: 'solid',
                    },
                    '&:active': {
                      backgroundColor: theme.palette.action.selected,
                      transform: 'scale(0.98)',
                    },
                  }}
                  onClick={e => onSlotClick(e, firstAppointment)}
                >
                  {slotAppointments.map(appt => (
                    <Box
                      key={appt.id}
                      sx={{
                        position: 'absolute',
                        top: 4,
                        left: 4,
                        right: 4,
                        bottom: 4,
                        backgroundColor: appt.appointmentType?.colorCode || theme.palette.primary.main,
                        color: '#fff',
                        borderRadius: 1,
                        px: 1,
                        py: 0.5,
                        fontSize: '0.75rem',
                        overflow: 'hidden',
                        whiteSpace: 'nowrap',
                        textOverflow: 'ellipsis',
                        zIndex: 1,
                      }}
                      aria-label={`Appointment: ${appt.client?.firstName} ${appt.client?.lastName} - ${appt.appointmentType?.name}`}
                    >
                      {appt.client?.firstName} {appt.client?.lastName} - {appt.appointmentType?.name}
                    </Box>
                  ))}
                </Box>
              );
            })}
          </Box>
        </Box>
      </Box>
    </Paper>
  );
};

export default DayView;
