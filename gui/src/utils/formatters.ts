import { Currency } from '../models/appointmentType/Currency';

/**
 * Formats duration from minutes to human-readable format.
 *
 * @param {number} minutes - Duration in minutes
 * @returns {string} Formatted duration string
 */
export const formatDuration = (minutes: number): string => {
  const hours = Math.floor(minutes / 60);
  const remainingMinutes = minutes % 60;

  if (hours > 0 && remainingMinutes > 0) {
    return `${hours}h ${remainingMinutes}m`;
  } else if (hours > 0) {
    return `${hours}h`;
  } else {
    return `${remainingMinutes}m`;
  }
};

/**
 * Formats price with currency using Intl.NumberFormat.
 *
 * @param {number} price - Price amount
 * @param {Currency} currency - Currency type
 * @returns {string} Formatted price string
 */
export const formatPrice = (price: number, currency: Currency): string => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: currency,
  }).format(price);
};
