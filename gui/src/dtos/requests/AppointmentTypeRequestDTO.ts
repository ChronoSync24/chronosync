import { Currency } from '../../models/appointmentType/Currency';
import { Firm } from '../../models/Firm';

/**
 * Appointment type request DTO.
 */
export interface AppointmentTypeRequestDTO {
  id: number;
  name: string;
  durationMinutes: number;
  price: number;
  colorCode: string;
  currency: Currency;
}
