import { AppointmentRequestDTO } from '../dtos/requests/AppointmentRequestDTO';
import { Appointment } from '../models/Appointment';
import { apiClient } from '../utils/ApiClient';

const ENDPOINT_PREFIX = '/appointment';

/**
 * Creates a new appointment type.
 *
 * @param {AppointmentRequestDTO} request - Appointment creation request
 * @returns {Promise<Appointment>} - Promise that resolves to the created appointment.
 *
 * @throws {Error} - Throws an error if the creation fails.
 */
export const create = async (request: AppointmentRequestDTO): Promise<Appointment> => {
    try {
        const response = await apiClient<Appointment>(`${ENDPOINT_PREFIX}/create`, {
            method: 'POST',
            body: request,
        });

        return response;
    } catch (error) {
        throw new Error('Appointment creation failed.');
    }
};

/**
 * Updates an appointment.
 *
 * @param {AppointmentRequestDTO} request - Appointment update request
 * @returns {Promise<Appointment>} - Promise that resolves to the updated appointment.
 *
 * @throws {Error} - Throws an error if the update fails.
 */
export const update = async (request: AppointmentRequestDTO): Promise<Appointment> => {
    try {
        const response = await apiClient<Appointment>(`${ENDPOINT_PREFIX}`, {
            method: 'PUT',
            body: request,
        });

        return response;
    } catch (error) {
        throw new Error('Appointment update failed.');
    }
};

/**
 * Deletes an appointment.
 *
 * @param {number} id - Appointment id
 *
 * @throws {Error} - Throws an error if the creation fails.
 */
export const remove = async (id: number): Promise<void> => {
    try {
        await apiClient<Appointment>(`${ENDPOINT_PREFIX}/${id}`, {
            method: 'DELETE',
        });
    } catch (error) {
        throw new Error('Appointment deletion failed.');
    }
};
