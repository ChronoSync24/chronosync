import { AppointmentTypeRequestDTO } from '../dtos/requests/AppointmentTypeRequestDTO';
import { PaginatedAppointmentTypeRequestDTO } from '../dtos/requests/PaginatedAppointmentTypeRequestDTO';
import { AppointmentType } from '../models/appointmentType/AppointmentType';
import { PageableResponse } from '../models/BaseEntity';
import { apiClient } from '../utils/ApiClient';

const ENDPOINT_PREFIX = '/appointment-type';

/**
 * Creates a new appointment type.
 *
 * @param {AppointmentTypeRequestDTO} request - Appointment type creation request
 * @returns {Promise<AppointmentType>} - Promise that resolves to the created appointment type.
 *
 * @throws {Error} - Throws an error if the creation fails.
 */
export const create = async (
  request: AppointmentTypeRequestDTO
): Promise<AppointmentType> => {
  try {
    const response = await apiClient<AppointmentType>(
      `${ENDPOINT_PREFIX}/create`,
      {
        method: 'POST',
        headers: {
          Authorization: 'Bearer ' + localStorage.getItem('token'),
          'Content-Type': 'application/json',
        },
        body: request,
      }
    );

    return response;
  } catch (error) {
    throw new Error('Appointment type creation failed.');
  }
};

/**
 * Updates appointment type.
 *
 * @param {AppointmentTypeRequestDTO} request - Appointment type update request
 * @returns {Promise<AppointmentType>} - Promise that resolves to the updated appointment type.
 *
 * @throws {Error} - Throws an error if the update fails.
 */
export const update = async (
  request: AppointmentTypeRequestDTO
): Promise<AppointmentType> => {
  try {
    const response = await apiClient<AppointmentType>(`${ENDPOINT_PREFIX}`, {
      method: 'PUT',
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('token'),
        'Content-Type': 'application/json',
      },
      body: request,
    });

    return response;
  } catch (error) {
    throw new Error('Appointment type update failed.');
  }
};

/**
 * Deletes an appointment type.
 *
 * @param {number} id - Appointment type id
 *
 * @throws {Error} - Throws an error if the creation fails.
 */
export const remove = async (id: number): Promise<void> => {
  try {
    await apiClient<AppointmentType>(`${ENDPOINT_PREFIX}?id=${id}`, {
      method: 'DELETE',
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('token'),
        'Content-Type': 'application/json',
      },
    });
  } catch (error) {
    throw new Error('Appointment type deletion failed.');
  }
};

/**
 * Fetches appointment types.
 *
 * @param {PaginatedAppointmentTypeRequestDTO} request - Paginated Appointment type get request
 * @returns {Promise<PageableResponse<AppointmentType>>} - Promise that resolves to the paginated appointment types.
 *
 * @throws {Error} - Throws an error if the fetching fails.
 */
export const get = async (
  request: PaginatedAppointmentTypeRequestDTO = {
    name: '',
    page: 0,
    pageSize: 10,
  }
): Promise<PageableResponse<AppointmentType>> => {
  try {
    const response = await apiClient<PageableResponse<AppointmentType>>(
      `${ENDPOINT_PREFIX}/search`,
      {
        method: 'POST',
        headers: {
          Authorization: 'Bearer ' + localStorage.getItem('token'),
          'Content-Type': 'application/json',
        },
        body: request,
      }
    );

    return response;
  } catch (error) {
    throw new Error('Appointment type creation failed.');
  }
};
