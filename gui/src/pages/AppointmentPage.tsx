import React from 'react';
import {
  Box,
  Typography,
  Paper,
  useTheme,
  Theme,
  IconButton,
  Button,
  ButtonGroup,
  Collapse
} from '@mui/material';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import FilterAltOutlinedIcon from '@mui/icons-material/FilterAltOutlined';
import Filters, { FilterField } from '../components/Filters';
import PrimaryButton from '../components/PrimaryButton';
import RadialMenu from '../components/RadialMenu';

const AppointmentPage: React.FC = () => {
  const theme: Theme = useTheme();

  // State for current week and view type
  const [currentDate, setCurrentDate] = React.useState(new Date());
  const [viewType, setViewType] = React.useState<'day' | 'week' | 'month'>('week');
  const [selectedDate, setSelectedDate] = React.useState<Date | null>(null);

  // Filters state
  const [isFiltersOpen, setIsFiltersOpen] = React.useState(false);
  const [filterValues, setFilterValues] = React.useState<Record<string, any>>({});

  // Radial Menu state
  const [radialMenuOpen, setRadialMenuOpen] = React.useState(false);
  const [radialMenuPos, setRadialMenuPos] = React.useState({ x: 0, y: 0 });
  const [radialMenuAppointment, setRadialMenuAppointment] = React.useState<any | null>(null);

  // Get current week range
  function getWeekStart(date: Date): Date {
    const d = new Date(date);
    const day = d.getDay();
    const diff = d.getDate() - day + (day === 0 ? -6 : 1); // adjust when day is sunday
    return new Date(d.setDate(diff));
  }

  function getWeekEnd(date: Date): Date {
    const start = getWeekStart(date);
    return new Date(start.getFullYear(), start.getMonth(), start.getDate() + 6);
  }

  function formatDate(date: Date): string {
    return date.toLocaleDateString(undefined, { year: 'numeric', month: 'short', day: 'numeric' });
  }

  const weekStart = getWeekStart(currentDate);
  const weekEnd = getWeekEnd(currentDate);
  const weekRange = `${formatDate(weekStart)} - ${formatDate(weekEnd)}`;

  // Mock appointments for this week
  const mockAppointments = [
    {
      id: 1,
      note: 'Initial consultation for new project',
      startDateTime: new Date(weekStart.getFullYear(), weekStart.getMonth(), weekStart.getDate() + 1, 10, 0).toISOString(), // Tuesday 10:00
      endDateTime: new Date(weekStart.getFullYear(), weekStart.getMonth(), weekStart.getDate() + 1, 11, 0).toISOString(),   // Tuesday 11:00
      employee: {
        id: 1,
        username: 'jdoe',
        password: '',
        role: 'EMPLOYEE',
        isLocked: false,
        isEnabled: true,
        firstName: 'John',
        lastName: 'Doe',
        identificationNumber: '123456789',
        address: '123 Main St',
        phone: '555-1234',
        email: 'jdoe@example.com',
        firm: {
          id: 1,
          name: 'Acme Corp'
        }
      },
      client: {
        id: 1,
        firstName: 'Alice',
        lastName: 'Smith',
        email: 'alice.smith@example.com',
        phone: '555-9876',
        firm: {
          id: 1,
          name: 'Acme Corp'
        }
      },
      appointmentType: {
        id: 1,
        name: 'Consultation',
        durationMinutes: 60,
        price: 100,
        colorCode: '#1976d2',
        currency: 'USD',
        firm: {
          id: 1,
          name: 'Acme Corp'
        }
      },
      firm: {
        id: 1,
        name: 'Acme Corp'
      },
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      createdBy: undefined,
      updatedBy: undefined
    },
    {
      id: 2,
      note: 'Follow-up appointment',
      startDateTime: new Date(weekStart.getFullYear(), weekStart.getMonth(), weekStart.getDate() + 3, 12, 30).toISOString(), // Thursday 15:30
      endDateTime: new Date(weekStart.getFullYear(), weekStart.getMonth(), weekStart.getDate() + 3, 13, 0).toISOString(),   // Thursday 16:00
      employee: {
        id: 2,
        username: 'msmith',
        password: '',
        role: 'EMPLOYEE',
        isLocked: false,
        isEnabled: true,
        firstName: 'Mary',
        lastName: 'Smith',
        identificationNumber: '987654321',
        address: '456 Oak Ave',
        phone: '555-5678',
        email: 'msmith@example.com',
        firm: {
          id: 2,
          name: 'Beta LLC'
        }
      },
      client: {
        id: 2,
        firstName: 'Bob',
        lastName: 'Johnson',
        email: 'bob.johnson@example.com',
        phone: '555-4321',
        firm: {
          id: 2,
          name: 'Beta LLC'
        }
      },
      appointmentType: {
        id: 2,
        name: 'Follow-up',
        durationMinutes: 30,
        price: 60,
        colorCode: '#43a047',
        currency: 'USD',
        firm: {
          id: 2,
          name: 'Beta LLC'
        }
      },
      firm: {
        id: 2,
        name: 'Beta LLC'
      },
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      createdBy: undefined,
      updatedBy: undefined
    }
  ];

  // Generate time slots from 08:00 to 23:30 in 30-minute intervals
  const generateTimeSlots = () => {
    const slots = [];
    for (let hour = 8; hour <= 18; hour++) {
      // Add full hour slot
      slots.push(`${hour.toString().padStart(2, '0')}:00`);
      // Add half hour slot
      slots.push(`${hour.toString().padStart(2, '0')}:30`);
    }
    return slots;
  };

  const timeSlots = generateTimeSlots();
  console.log('Time slots generated:', timeSlots.length, 'slots from', timeSlots[0], 'to', timeSlots[timeSlots.length - 1]);

  // Navigation functions
  const goToPreviousWeek = () => {
    const newDate = new Date(currentDate);
    newDate.setDate(currentDate.getDate() - 7);
    setCurrentDate(newDate);
  };

  const goToNextWeek = () => {
    const newDate = new Date(currentDate);
    newDate.setDate(currentDate.getDate() + 7);
    setCurrentDate(newDate);
  };

  const goToToday = () => {
    setCurrentDate(new Date());
  };

  // Get days of the current week
  const getDaysOfWeek = () => {
    const days = [];
    const start = getWeekStart(currentDate);

    for (let i = 0; i < 7; i++) {
      const day = new Date(start);
      day.setDate(start.getDate() + i);
      days.push(day);
    }

    return days;
  };

  const daysOfWeek = getDaysOfWeek();

  // Get days of the current month for month view
  const getDaysOfMonth = () => {
    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const daysInMonth = lastDay.getDate();

    // Get the day of week for the first day (0 = Sunday, 1 = Monday, etc.)
    const firstDayOfWeek = firstDay.getDay();
    const startOffset = firstDayOfWeek === 0 ? 6 : firstDayOfWeek - 1; // Adjust for Monday start

    const days = [];

    // Add empty cells for days before the first day of the month
    for (let i = 0; i < startOffset; i++) {
      days.push(null);
    }

    // Add all days of the month
    for (let i = 1; i <= daysInMonth; i++) {
      days.push(new Date(year, month, i));
    }

    return days;
  };

  const daysOfMonth = getDaysOfMonth();

  // Format time for display
  const formatTime = (time: string) => {
    const [hours, minutes] = time.split(':');
    const hour = parseInt(hours);
    const ampm = hour >= 12 ? 'PM' : 'AM';
    const displayHour = hour === 0 ? 12 : hour > 12 ? hour - 12 : hour;
    return `${displayHour}:${minutes} ${ampm}`;
  };

  // Define filter fields for appointments
  const filterFields: FilterField[] = [
    {
      key: 'clientName',
      label: 'Client Name',
      type: 'text',
      placeholder: 'Search by client name...',
    },
    {
      key: 'appointmentType',
      label: 'Appointment Type',
      type: 'select',
      options: [
        { value: 'consultation', label: 'Consultation' },
        { value: 'follow-up', label: 'Follow-up' },
        { value: 'emergency', label: 'Emergency' },
        { value: 'checkup', label: 'Checkup' },
      ],
    },
    {
      key: 'status',
      label: 'Status',
      type: 'select',
      options: [
        { value: 'scheduled', label: 'Scheduled' },
        { value: 'confirmed', label: 'Confirmed' },
        { value: 'completed', label: 'Completed' },
        { value: 'cancelled', label: 'Cancelled' },
      ],
    },
  ];

  // Filter handlers
  const handleFilterChange = (key: string, value: any) => {
    setFilterValues(prev => ({ ...prev, [key]: value }));
  };

  const handleClearFilters = () => {
    setFilterValues({});
  };

  const toggleFilters = () => {
    setIsFiltersOpen(!isFiltersOpen);
  };

  // Navigation functions for different views
  const goToPreviousPeriod = () => {
    const newDate = new Date(currentDate);
    if (viewType === 'day') {
      newDate.setDate(currentDate.getDate() - 1);
    } else if (viewType === 'week') {
      newDate.setDate(currentDate.getDate() - 7);
    } else if (viewType === 'month') {
      newDate.setMonth(currentDate.getMonth() - 1);
    }
    setCurrentDate(newDate);
  };

  const goToNextPeriod = () => {
    const newDate = new Date(currentDate);
    if (viewType === 'day') {
      newDate.setDate(currentDate.getDate() + 1);
    } else if (viewType === 'week') {
      newDate.setDate(currentDate.getDate() + 7);
    } else if (viewType === 'month') {
      newDate.setMonth(currentDate.getMonth() + 1);
    }
    setCurrentDate(newDate);
  };

  // Get display date range based on view type
  const getDisplayDateRange = () => {
    if (viewType === 'day') {
      return formatDate(currentDate);
    } else if (viewType === 'week') {
      return weekRange;
    } else if (viewType === 'month') {
      return currentDate.toLocaleDateString('en-US', {
        month: 'long',
        year: 'numeric'
      });
    }
    return '';
  };

  // Helper to get adjusted menu position so it stays in viewport
  const getAdjustedMenuPosition = (x: number, y: number) => {
    const radius = 70;
    const menuDiameter = radius * 2 + 48; // 48 is button size
    const padding = 8;
    let left = x;
    let top = y;
    if (typeof window !== 'undefined') {
      if (left + menuDiameter > window.innerWidth - padding) {
        left = window.innerWidth - menuDiameter - padding;
      }
      if (left < padding) left = padding;
      if (top + menuDiameter > window.innerHeight - padding) {
        top = window.innerHeight - menuDiameter - padding;
      }
      if (top < padding) top = padding;
    }
    return { x: left, y: top };
  };

  // Helper to open radial menu
  const handleSlotClick = (e: React.MouseEvent, appointment: any | null) => {
    e.preventDefault();
    e.stopPropagation();
    const pos = getAdjustedMenuPosition(e.clientX, e.clientY);
    setRadialMenuPos(pos);
    setRadialMenuAppointment(appointment);
    setRadialMenuOpen(true);
  };

  return (
    <Box sx={{ p: 3, height: '100vh', overflow: 'hidden' }}>
      {/* Header with navigation and view controls */}
      <Box sx={{
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        mb: 3
      }}>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <Typography
            variant="h4"
            sx={{
              fontWeight: 600,
              color: theme.palette.text.primary
            }}
          >
            Calendar
          </Typography>

          {/* Period navigation */}
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
            <IconButton
              onClick={goToPreviousPeriod}
              sx={{
                color: theme.palette.secondary.main,
                '&:hover': { backgroundColor: theme.palette.action.hover }
              }}
            >
              <ChevronLeftIcon />
            </IconButton>

            <Typography
              variant="h6"
              sx={{
                fontWeight: 500,
                color: theme.palette.text.primary,
                minWidth: 200,
                textAlign: 'center'
              }}
            >
              {getDisplayDateRange()}
            </Typography>

            <IconButton
              onClick={goToNextPeriod}
              sx={{
                color: theme.palette.secondary.main,
                '&:hover': { backgroundColor: theme.palette.action.hover }
              }}
            >
              <ChevronRightIcon />
            </IconButton>
          </Box>
        </Box>

        {/* View type selector and filters */}
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
          <PrimaryButton
            onClick={goToToday}
            sx={{ minWidth: 80 }}
          >
            Today
          </PrimaryButton>

          <ButtonGroup variant="outlined" size="small">
            <Button
              variant={viewType === 'day' ? 'contained' : 'outlined'}
              onClick={() => setViewType('day')}
              color="secondary"
              sx={{
                minWidth: 60,
                ...(viewType === 'day' && {
                  backgroundColor: theme.palette.secondary.main,
                  color: theme.palette.secondary.contrastText,
                  '&:hover': { backgroundColor: theme.palette.secondary.dark }
                })
              }}
            >
              Day
            </Button>
            <Button
              variant={viewType === 'week' ? 'contained' : 'outlined'}
              onClick={() => setViewType('week')}
              color="secondary"
              sx={{
                minWidth: 60,
                ...(viewType === 'week' && {
                  backgroundColor: theme.palette.secondary.main,
                  color: theme.palette.secondary.contrastText,
                  '&:hover': { backgroundColor: theme.palette.secondary.dark }
                })
              }}
            >
              Week
            </Button>
            <Button
              variant={viewType === 'month' ? 'contained' : 'outlined'}
              onClick={() => setViewType('month')}
              color="secondary"
              sx={{
                minWidth: 60,
                ...(viewType === 'month' && {
                  backgroundColor: theme.palette.secondary.main,
                  color: theme.palette.secondary.contrastText,
                  '&:hover': { backgroundColor: theme.palette.secondary.dark }
                })
              }}
            >
              Month
            </Button>
          </ButtonGroup>

          {/*
          <IconButton 
            onClick={toggleFilters}
            sx={{
              borderRadius: '8px',
              color: theme.palette.secondary.main,
              border: `1px solid ${isFiltersOpen ? theme.palette.secondary.main : 'transparent'}`,
              '&:hover': {
                backgroundColor: theme.palette.background.paper,
                border: `1px solid ${theme.palette.secondary.main}`,
              }
            }}
          >
            <FilterAltOutlinedIcon fontSize='medium' />
          </IconButton>
          */}

        </Box>
      </Box>

      {/* Filters
      <Collapse in={isFiltersOpen}>
        <Box sx={{ mb: 3 }}>
          <Filters
            isOpen={isFiltersOpen}
            fields={filterFields}
            values={filterValues}
            onFilterChange={handleFilterChange}
            onClearFilters={handleClearFilters}
          />
        </Box>
      </Collapse>
      */}
      {/* Render different views based on viewType */}
      {viewType === 'day' && (
        <Paper
          elevation={2}
          sx={{
            height: 'calc(100vh - 140px)',
            overflow: 'hidden',
            borderRadius: 2
          }}
        >
          {/* Day View */}
          <Box sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
            {/* Day header */}
            <Box sx={{
              display: 'flex',
              minHeight: 60,
              borderBottom: `1px solid ${theme.palette.divider}`,
              backgroundColor: theme.palette.background.paper
            }}>
              <Box
                sx={{
                  width: '12.5%',
                  borderRight: `1px solid ${theme.palette.divider}`,
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center'
                }}
              >
                <Typography
                  variant="body2"
                  sx={{
                    fontWeight: 500,
                    color: theme.palette.text.secondary
                  }}
                >
                  Time
                </Typography>
              </Box>
              <Box
                sx={{
                  flex: 1,
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center'
                }}
              >
                <Typography
                  variant="h6"
                  sx={{
                    fontWeight: 600,
                    color: theme.palette.text.primary
                  }}
                >
                  {currentDate.toLocaleDateString('en-US', {
                    weekday: 'long',
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric'
                  })}
                </Typography>
              </Box>
            </Box>

            {/* Day grid */}
            <Box sx={{ display: 'flex', flex: 1 }}>
              {/* Time labels column */}
              <Box
                sx={{
                  width: '12.5%',
                  borderRight: `1px solid ${theme.palette.divider}`,
                  backgroundColor: theme.palette.background.paper,
                  overflowY: 'auto',
                  maxHeight: '100%'
                }}
              >
                {timeSlots.map((time, index) => (
                  <Box
                    key={time}
                    sx={{
                      height: 60,
                      borderBottom: index < timeSlots.length - 1 ? `1px solid ${theme.palette.divider}` : 'none',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      px: 1,
                      flexShrink: 0
                    }}
                  >
                    <Typography
                      variant="caption"
                      sx={{
                        color: theme.palette.text.secondary,
                        fontSize: '0.75rem',
                        fontWeight: 500
                      }}
                    >
                      {formatTime(time)}
                    </Typography>
                  </Box>
                ))}
              </Box>

              {/* Single day column */}
              <Box
                sx={{
                  flex: 1,
                  backgroundColor: theme.palette.background.default,
                  overflowY: 'auto',
                  maxHeight: '100%'
                }}
              >
                {timeSlots.map((time, timeIndex) => {
                  const [slotHour, slotMinute] = time.split(':').map(Number);
                  const slotStart = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate(), slotHour, slotMinute);
                  const slotEnd = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate(), slotHour, slotMinute + 30);
                  const slotAppointments = mockAppointments.filter(appt => {
                    const apptStart = new Date(appt.startDateTime);
                    return apptStart >= slotStart && apptStart < slotEnd;
                  });
                  const firstAppointment = slotAppointments[0] || null;
                  return (
                    <Box
                      key={time}
                      sx={{
                        height: 60,
                        borderBottom: timeIndex < timeSlots.length - 1 ? `1px solid ${theme.palette.divider}` : 'none',
                        borderLeft: `1px solid ${theme.palette.divider}`,
                        backgroundColor: theme.palette.background.paper,
                        cursor: 'pointer',
                        transition: 'all 0.2s ease-in-out',
                        flexShrink: 0,
                        position: 'relative',
                        '&:hover': {
                          backgroundColor: theme.palette.action.hover,
                          borderColor: theme.palette.secondary.main,
                          borderWidth: '2px',
                          borderStyle: 'solid'
                        },
                        '&:active': {
                          backgroundColor: theme.palette.action.selected,
                          transform: 'scale(0.98)'
                        }
                      }}
                      onClick={e => handleSlotClick(e, firstAppointment)}
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
                            zIndex: 1
                          }}
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
      )}

      {viewType === 'week' && (
        <Paper
          elevation={2}
          sx={{
            height: 'calc(100vh - 140px)',
            overflow: 'hidden',
            borderRadius: 2
          }}
        >
          <Box sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
            {/* Header row */}
            <Box sx={{ display: 'flex', minHeight: 60 }}>
              {/* Time column header (empty corner) */}
              <Box
                sx={{
                  width: '12.5%',
                  borderRight: `1px solid ${theme.palette.divider}`,
                  borderBottom: `1px solid ${theme.palette.divider}`,
                  backgroundColor: theme.palette.background.paper,
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center'
                }}
              >
                <Typography
                  variant="body2"
                  sx={{
                    fontWeight: 500,
                    color: theme.palette.text.secondary
                  }}
                >
                  Time
                </Typography>
              </Box>

              {/* Day headers */}
              {daysOfWeek.map((day, index) => (
                <Box
                  key={day.toISOString()}
                  sx={{
                    width: '12.5%',
                    borderRight: index < daysOfWeek.length - 1 ? `1px solid ${theme.palette.divider}` : 'none',
                    borderBottom: `1px solid ${theme.palette.divider}`,
                    backgroundColor: theme.palette.background.paper,
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center'
                  }}
                >
                  <Box sx={{ textAlign: 'center' }}>
                    <Typography
                      variant="body2"
                      sx={{
                        fontWeight: 600,
                        color: theme.palette.text.primary,
                        mb: 0.5
                      }}
                    >
                      {day.toLocaleDateString('en-US', { weekday: 'short' })}
                    </Typography>
                    <Typography
                      variant="caption"
                      sx={{
                        color: theme.palette.text.secondary,
                        display: 'block'
                      }}
                    >
                      {day.toLocaleDateString('en-US', {
                        month: 'short',
                        day: 'numeric'
                      })}
                    </Typography>
                  </Box>
                </Box>
              ))}
            </Box>

            {/* Calendar grid */}
            <Box sx={{ display: 'flex', flex: 1 }}>
              {/* Time labels column */}
              <Box
                sx={{
                  width: '12.5%',
                  borderRight: `1px solid ${theme.palette.divider}`,
                  backgroundColor: theme.palette.background.paper,
                  overflowY: 'auto',
                  maxHeight: '100%'
                }}
              >
                {timeSlots.map((time, index) => (
                  <Box
                    key={time}
                    sx={{
                      height: 60,
                      borderBottom: index < timeSlots.length - 1 ? `1px solid ${theme.palette.divider}` : 'none',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      px: 1,
                      flexShrink: 0
                    }}
                  >
                    <Typography
                      variant="caption"
                      sx={{
                        color: theme.palette.text.secondary,
                        fontSize: '0.75rem',
                        fontWeight: 500
                      }}
                    >
                      {formatTime(time)}
                    </Typography>
                  </Box>
                ))}
              </Box>

              {/* Day columns */}
              {daysOfWeek.map((day, dayIndex) => (
                <Box
                  key={day.toISOString()}
                  sx={{
                    width: '12.5%',
                    borderRight: dayIndex < daysOfWeek.length - 1 ? `1px solid ${theme.palette.divider}` : 'none',
                    backgroundColor: theme.palette.background.default,
                    overflowY: 'auto',
                    maxHeight: '100%'
                  }}
                >
                  {timeSlots.map((time, timeIndex) => {
                    // Parse slot time
                    const [slotHour, slotMinute] = time.split(':').map(Number);
                    const slotStart = new Date(day.getFullYear(), day.getMonth(), day.getDate(), slotHour, slotMinute);
                    const slotEnd = new Date(day.getFullYear(), day.getMonth(), day.getDate(), slotHour, slotMinute + 30);
                    // Find appointment(s) that start in this slot
                    const slotAppointments = mockAppointments.filter(appt => {
                      const apptStart = new Date(appt.startDateTime);
                      return apptStart >= slotStart && apptStart < slotEnd;
                    });
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
                          transition: 'all 0.2s ease-in-out',
                          flexShrink: 0,
                          position: 'relative',
                          '&:hover': {
                            backgroundColor: theme.palette.action.hover,
                            borderColor: theme.palette.secondary.main,
                            borderWidth: '2px',
                            borderStyle: 'solid'
                          },
                          '&:active': {
                            backgroundColor: theme.palette.action.selected,
                            transform: 'scale(0.98)'
                          }
                        }}
                        onClick={e => handleSlotClick(e, firstAppointment)}
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
                              zIndex: 1
                            }}
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
      )}

      {viewType === 'month' && (
        <Paper
          elevation={2}
          sx={{
            height: 'calc(100vh - 140px)',
            overflow: 'hidden',
            borderRadius: 2
          }}
        >
          {/* Month View */}
          <Box sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
            {/* Month header */}
            <Box sx={{
              display: 'flex',
              minHeight: 60,
              borderBottom: `1px solid ${theme.palette.divider}`,
              backgroundColor: theme.palette.background.paper
            }}>
              {['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'].map((dayName, index) => (
                <Box
                  key={dayName}
                  sx={{
                    flex: 1,
                    borderRight: index < 6 ? `1px solid ${theme.palette.divider}` : 'none',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center'
                  }}
                >
                  <Typography
                    variant="body2"
                    sx={{
                      fontWeight: 600,
                      color: theme.palette.text.primary
                    }}
                  >
                    {dayName}
                  </Typography>
                </Box>
              ))}
            </Box>

            {/* Month grid */}
            <Box sx={{
              display: 'grid',
              gridTemplateColumns: 'repeat(7, 1fr)',
              flex: 1,
              overflow: 'auto'
            }}>
              {daysOfMonth.map((day, index) => (
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
                  onClick={() => {
                    if (day) {
                      setSelectedDate(day);
                      setViewType('day');
                      setCurrentDate(day);
                    }
                  }}
                >
                  {day && (
                    <Typography
                      variant="body2"
                      sx={{
                        fontWeight: 500,
                        color: theme.palette.text.primary,
                        mb: 1
                      }}
                    >
                      {day.getDate()}
                    </Typography>
                  )}
                </Box>
              ))}
            </Box>
          </Box>
        </Paper>
      )}
      {radialMenuOpen && (
        <RadialMenu
          position={radialMenuPos}
          appointmentData={radialMenuAppointment}
          onOptionSelect={key => {
            setRadialMenuOpen(false);
            // eslint-disable-next-line no-console
            console.log('Selected option:', key, 'Appointment:', radialMenuAppointment?.id);
          }}
          onClose={() => setRadialMenuOpen(false)}
        />
      )}
    </Box>
  );
};

export default AppointmentPage;
