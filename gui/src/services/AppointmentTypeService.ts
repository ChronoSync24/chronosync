import { AppointmentTypeRequestDTO } from '../dtos/requests/AppointmentTypeRequestDTO';
import { AppointmentType } from '../models/appointmentType/AppointmentType';
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
export const create = async (request: AppointmentTypeRequestDTO): Promise<AppointmentType> => {
	try {
		const response = await apiClient<AppointmentType>(`${ENDPOINT_PREFIX}/create`, {
			method: 'POST',
			body: request,
		});

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
export const update = async (request: AppointmentTypeRequestDTO): Promise<AppointmentType> => {
	try {
		const response = await apiClient<AppointmentType>(`${ENDPOINT_PREFIX}`, {
			method: 'PUT',
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
		await apiClient<AppointmentType>(`${ENDPOINT_PREFIX}/${id}`, {
			method: 'DELETE',
		});
	} catch (error) {
		throw new Error('Appointment type deletion failed.');
	}
};
