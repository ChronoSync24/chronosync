import React from 'react';
import { Box, Paper, Typography, useTheme } from '@mui/material';
import { Appointment } from '../../models/Appointment';
import { getSlotAppointments } from './utils';

interface WeekViewProps {
  days: Date[];
  timeSlots: string[];
  appointments: Appointment[];
  onSlotClick: (e: React.MouseEvent, appointment: Appointment | null) => void;
  formatTime: (time: string) => string;
}

const WeekView: React.FC<WeekViewProps> = ({ days, timeSlots, appointments, onSlotClick, formatTime }) => {
  const theme = useTheme();
  const slotColRefs = React.useRef<(HTMLDivElement | null)[]>([]);

  // Scroll to current time on mount for today column
  React.useEffect(() => {
    const todayIdx = days.findIndex(day => day.toDateString() === new Date().toDateString());
    if (todayIdx !== -1 && slotColRefs.current[todayIdx]) {
      const now = new Date();
      const nowMinutes = now.getHours() * 60 + now.getMinutes();
      let targetIndex = timeSlots.findIndex(slot => {
        const [h, m] = slot.split(':').map(Number);
        return h * 60 + m >= nowMinutes;
      });
      if (targetIndex === -1) targetIndex = 0;
      const scrollTo = Math.max(0, targetIndex - 1) * 60;
      slotColRefs.current[todayIdx]!.scrollTop = scrollTo;
    }
  }, [days, timeSlots]);

  return (
    <Paper elevation={2} sx={{ height: 'calc(100vh - 140px)', overflow: 'auto', borderRadius: 2 }}>
      <Box sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
        <Box sx={{ display: 'flex', minHeight: 60 }}>
          <Box sx={{ width: '12.5%', borderRight: `1px solid ${theme.palette.divider}`, borderBottom: `1px solid ${theme.palette.divider}`, backgroundColor: theme.palette.background.paper, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
            <Typography variant="body2" sx={{ fontWeight: 500, color: theme.palette.text.secondary }}>
              Time
            </Typography>
          </Box>
          {days.map((day, index) => (
            <Box key={day.toISOString()} sx={{ width: '12.5%', borderRight: index < days.length - 1 ? `1px solid ${theme.palette.divider}` : 'none', borderBottom: `1px solid ${theme.palette.divider}`, backgroundColor: theme.palette.background.paper, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <Box sx={{ textAlign: 'center' }}>
                <Typography variant="body2" sx={{ fontWeight: 600, mb: 0.5 }}>
                  {day.toLocaleDateString('en-US', { weekday: 'short' })}
                </Typography>
                <Typography variant="caption" sx={{ color: theme.palette.text.secondary }}>
                  {day.toLocaleDateString('en-US', { month: 'short', day: 'numeric' })}
                </Typography>
              </Box>
            </Box>
          ))}
        </Box>

        <Box sx={{ display: 'flex', flex: 1, minHeight: 0 }}>
          <Box sx={{ width: '12.5%', borderRight: `1px solid ${theme.palette.divider}`, backgroundColor: theme.palette.background.paper, overflowY: 'auto' }}>
            {timeSlots.map((time, index) => (
              <Box key={time} sx={{ height: 60, borderBottom: index < timeSlots.length - 1 ? `1px solid ${theme.palette.divider}` : 'none', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <Typography variant="caption" sx={{ fontWeight: 500, color: theme.palette.text.secondary }}>
                  {formatTime(time)}
                </Typography>
              </Box>
            ))}
          </Box>

          {days.map((day, dayIndex) => (
            <Box
              key={day.toISOString()}
              ref={(el: HTMLDivElement | null) => { slotColRefs.current[dayIndex] = el; }}
              sx={{ width: '12.5%', borderRight: dayIndex < days.length - 1 ? `1px solid ${theme.palette.divider}` : 'none', backgroundColor: theme.palette.background.default, overflowY: 'auto' }}
            >
              {timeSlots.map((time, timeIndex) => {
                const slotAppointments = React.useMemo(() => getSlotAppointments({
                  time,
                  date: day,
                  appointments,
                }), [time, day, appointments]);
                const firstAppointment = slotAppointments[0] || null;

                return (
                  <Box
                    key={`${day.toISOString()}-${time}`}
                    sx={{
                      height: 60,
                      borderBottom: timeIndex < timeSlots.length - 1 ? `1px solid ${theme.palette.divider}` : 'none',
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
          ))}
        </Box>
      </Box>
    </Paper>
  );
};

export default WeekView;
