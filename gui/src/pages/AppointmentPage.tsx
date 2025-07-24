import React from 'react';
import { Box } from '@mui/material';
import { useTheme } from '@mui/material/styles';
import CalendarHeader from '../components/calendar/CalendarHeader';
import DayView from '../components/calendar/DayView';
import WeekView from '../components/calendar/WeekView';
import MonthView from '../components/calendar/MonthView';
import RadialMenu from '../components/RadialMenu';
import { Appointment } from '../models/Appointment';
import { mockAppointments } from '../components/calendar/testAppointments';
import { getSlotAppointments, getDynamicTimeSlots, formatDateDMY } from '../components/calendar/utils';

const AppointmentPage: React.FC = () => {
  const theme = useTheme();
  const [viewType, setViewType] = React.useState<'day' | 'week' | 'month'>('week');
  const [currentDate, setCurrentDate] = React.useState(new Date());
  const [radialMenuOpen, setRadialMenuOpen] = React.useState(false);
  const [radialMenuPos, setRadialMenuPos] = React.useState({ x: 0, y: 0 });
  const [radialMenuAppointment, setRadialMenuAppointment] = React.useState<Appointment | null>(null);

  const formatTime = (time: string) => {
    const [hours, minutes] = time.split(':');
    const hour = parseInt(hours);
    const ampm = hour >= 12 ? 'PM' : 'AM';
    const displayHour = hour === 0 ? 12 : hour > 12 ? hour - 12 : hour;
    return `${displayHour}:${minutes} ${ampm}`;
  };

  const getWeekStart = (date: Date) => {
    const d = new Date(date);
    const day = d.getDay();
    const diff = d.getDate() - day + (day === 0 ? -6 : 1);
    return new Date(d.setDate(diff));
  };

  const getDaysOfWeek = () => {
    const start = getWeekStart(currentDate);
    return Array.from({ length: 7 }, (_, i) => new Date(start.getFullYear(), start.getMonth(), start.getDate() + i));
  };

  const getDaysOfMonth = () => {
    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const days: (Date | null)[] = [];
    const startOffset = (firstDay.getDay() + 6) % 7;
    for (let i = 0; i < startOffset; i++) days.push(null);
    for (let i = 1; i <= lastDay.getDate(); i++) days.push(new Date(year, month, i));
    return days;
  };

  const getDisplayDateRange = () => {
    if (viewType === 'day') {
      return formatDateDMY(currentDate);
    } else if (viewType === 'week') {
      const start = getWeekStart(currentDate);
      const end = new Date(start);
      end.setDate(start.getDate() + 6);
      return `${formatDateDMY(start)} - ${formatDateDMY(end)}`;
    } else {
      return currentDate.toLocaleDateString('en-US', { month: 'long', year: 'numeric' });
    }
  };

  const goToPreviousPeriod = () => {
    const newDate = new Date(currentDate);
    if (viewType === 'day') newDate.setDate(newDate.getDate() - 1);
    else if (viewType === 'week') newDate.setDate(newDate.getDate() - 7);
    else newDate.setMonth(newDate.getMonth() - 1);
    setCurrentDate(newDate);
  };

  const goToNextPeriod = () => {
    const newDate = new Date(currentDate);
    if (viewType === 'day') newDate.setDate(newDate.getDate() + 1);
    else if (viewType === 'week') newDate.setDate(newDate.getDate() + 7);
    else newDate.setMonth(newDate.getMonth() + 1);
    setCurrentDate(newDate);
  };

  const goToToday = () => setCurrentDate(new Date());

  const getAdjustedMenuPosition = (x: number, y: number) => {
    const radius = 70;
    const menuDiameter = radius * 2 + 48;
    const padding = 8;
    let left = x;
    let top = y;
    if (typeof window !== 'undefined') {
      if (left + menuDiameter > window.innerWidth - padding) left = window.innerWidth - menuDiameter - padding;
      if (left < padding) left = padding;
      if (top + menuDiameter > window.innerHeight - padding) top = window.innerHeight - menuDiameter - padding;
      if (top < padding) top = padding;
    }
    return { x: left, y: top };
  };

  const handleSlotClick = (e: React.MouseEvent, appointment: Appointment | null) => {
    e.preventDefault();
    e.stopPropagation();
    const pos = getAdjustedMenuPosition(e.clientX, e.clientY);
    setRadialMenuPos(pos);
    setRadialMenuAppointment(appointment);
    setRadialMenuOpen(true);
  };

  return (
    <Box sx={{ p: 3, height: '100vh', overflow: 'hidden' }}>
      <CalendarHeader
        viewType={viewType}
        currentDate={currentDate}
        onViewTypeChange={setViewType}
        onPrevious={goToPreviousPeriod}
        onNext={goToNextPeriod}
        onToday={goToToday}
        getDisplayDateRange={getDisplayDateRange}
      />

      {viewType === 'day' && (
        <DayView
          date={currentDate}
          timeSlots={getDynamicTimeSlots(mockAppointments, currentDate)}
          appointments={mockAppointments}
          onSlotClick={handleSlotClick}
          formatTime={formatTime}
        />
      )}

      {viewType === 'week' && (
        <WeekView
          days={getDaysOfWeek()}
          timeSlots={getDynamicTimeSlots(mockAppointments, currentDate)}
          appointments={mockAppointments}
          onSlotClick={handleSlotClick}
          formatTime={formatTime}
        />
      )}

      {viewType === 'month' && (
        <MonthView
          daysOfMonth={getDaysOfMonth()}
          appointments={mockAppointments}
          onDaySelect={day => {
            setCurrentDate(day);
            setViewType('day');
          }}
        />
      )}

      {radialMenuOpen && (
        <RadialMenu
          position={radialMenuPos}
          appointmentData={radialMenuAppointment}
          onOptionSelect={(key) => {
            setRadialMenuOpen(false);
            console.log('Selected option:', key);
          }}
          onClose={() => setRadialMenuOpen(false)}
        />
      )}
    </Box>
  );
};

export default AppointmentPage;