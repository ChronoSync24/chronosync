import { Appointment } from '../../models/Appointment';

interface GetSlotAppointmentsParams {
  time: string;
  date: Date;
  appointments: Appointment[];
}

/**
 * Returns appointments that start within the given slot (30 min window).
 */
export function getSlotAppointments({ time, date, appointments }: GetSlotAppointmentsParams): Appointment[] {
  const [hour, minute] = time.split(':').map(Number);
  const slotStart = new Date(date.getFullYear(), date.getMonth(), date.getDate(), hour, minute);
  const slotEnd = new Date(slotStart);
  slotEnd.setMinutes(slotStart.getMinutes() + 30);
  return appointments.filter(appt => {
    const apptStart = new Date(appt.startDateTime);
    return apptStart >= slotStart && apptStart < slotEnd;
  });
}

/**
 * Generate dynamic time slots based on earliest start and latest end of appointments.
 * Always includes at least 08:00 to 18:00, but expands if needed.
 */
export function getDynamicTimeSlots(appointments: Appointment[], date: Date): string[] {
  let minHour = 8;
  let maxHour = 18;
  appointments.forEach(appt => {
    const start = new Date(appt.startDateTime);
    const end = new Date(appt.endDateTime);
    if (
      start.toDateString() === date.toDateString() ||
      end.toDateString() === date.toDateString()
    ) {
      minHour = Math.min(minHour, start.getHours());
      maxHour = Math.max(maxHour, end.getHours() + (end.getMinutes() > 0 ? 1 : 0));
    }
  });
  minHour = Math.max(0, minHour);
  maxHour = Math.min(23, maxHour);
  const slots: string[] = [];
  for (let hour = minHour; hour <= maxHour; hour++) {
    slots.push(`${hour.toString().padStart(2, '0')}:00`);
    slots.push(`${hour.toString().padStart(2, '0')}:30`);
  }
  return slots;
}

/**
 * Determine appointment state: 'pre', 'during', 'post' based on now.
 */
export function getAppointmentState(appt: Appointment): 'pre' | 'during' | 'post' {
  const now = new Date();
  const start = new Date(appt.startDateTime);
  const end = new Date(appt.endDateTime);
  if (now < start) return 'pre';
  if (now >= start && now <= end) return 'during';
  return 'post';
}

/**
 * Get appointment dots for a given day (for month view)
 * Returns array of color codes for that day
 */
export function getAppointmentDotsForDay(appointments: Appointment[], day: Date): string[] {
  const dots: string[] = [];
  appointments.forEach(appt => {
    const apptDate = new Date(appt.startDateTime);
    if (
      apptDate.getFullYear() === day.getFullYear() &&
      apptDate.getMonth() === day.getMonth() &&
      apptDate.getDate() === day.getDate()
    ) {
      if (appt.appointmentType?.colorCode && !dots.includes(appt.appointmentType.colorCode)) {
        dots.push(appt.appointmentType.colorCode);
      }
    }
  });
  return dots;
}

/**
 * Format date as DD.MM.YYYY
 */
export function formatDateDMY(date: Date): string {
  return `${date.getDate().toString().padStart(2, '0')}.${(date.getMonth()+1).toString().padStart(2, '0')}.${date.getFullYear()}`;
}

//SLOT ENDS SHOULD BE APPOINTMENT ENDS