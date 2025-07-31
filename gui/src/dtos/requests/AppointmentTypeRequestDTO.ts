import { Currency } from '../../models/appointmentType/Currency';

/**
 * Appointment type request DTO.
 *
 * @property {number} id - The ID of the appointment type
 * @property {string} name - The name of the appointment type
 * @property {number} durationMinutes - The duration of the appointment type in minutes
 * @property {number} price - The price of the appointment type
 * @property {string} colorCode - The color code of the appointment type (hex code)
 * @property {Currency} currency - The currency of the appointment type
 */
export interface AppointmentTypeRequestDTO {
  id: number;
  name: string;
  durationMinutes: number;
  price: number;
  colorCode: string;
  currency: Currency;
}
