import { apiClient } from "../utils/ApiClient";
import { Client } from "../models/Client";
import { ClientRequestDTO } from "../dtos/requests/ClientRequestDTO";

const ENDPOINT_PREFIX = '/client';

/**
 * Creates a new client.
 *
 * @param {ClientRequestDTO} request - Client creation request
 * @returns {Promise<Client>} - Promise that resolves to the created client.
 *
 * @throws {Error} - Throws an error if the creation fails.
 */
export const create = async (request: ClientRequestDTO): Promise<Client> => {
    try {
        const response = await apiClient<Client>(`${ENDPOINT_PREFIX}/create`, {
            method: 'POST',
            body: request,
        });

        return response;
    } catch (error) {
        throw new Error('Client creation failed.');
    }
};

/**
 * Updates a client.
 *
 * @param {ClientRequestDTO} request - Client update request
 * @returns {Promise<Client>} - Promise that resolves to the updated client.
 *
 * @throws {Error} - Throws an error if the update fails.
 */
export const update = async (request: ClientRequestDTO): Promise<Client> => {
    try {
        const response = await apiClient<Client>(`${ENDPOINT_PREFIX}`, {
            method: 'PUT',
            body: request,
        });

        return response;
    } catch (error) {
        throw new Error('Client update failed.');
    }
};

/**
 * Deletes a client.
 *
 * @param {number} id - Client id
 *
 * @throws {Error} - Throws an error if the creation fails
 */
export const remove = async (id: number): Promise<void> => {
    try {
        await apiClient<Client>(`${ENDPOINT_PREFIX}/${id}`, {
            method: 'DELETE',
        });
    } catch (error) {
        throw new Error('Client deletion failed.');
    }
};