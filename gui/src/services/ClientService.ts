import { apiClient } from '../utils/ApiClient';
import { Client } from '../models/Client';
import { ClientRequestDTO } from '../dtos/requests/ClientRequestDTO';
import { PaginatedClientRequestDTO } from '../dtos/requests/PaginatedClientRequestDTO';
import { PageableResponse } from '../models/BaseEntity';

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
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('token'),
        'Content-Type': 'application/json',
      },
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
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('token'),
        'Content-Type': 'application/json',
      },
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
 * @throws {Error} - Throws an error if the deletion fails
 */
export const remove = async (id: number): Promise<void> => {
  try {
    await apiClient<Client>(`${ENDPOINT_PREFIX}?id=${id}`, {
      method: 'DELETE',
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('token'),
        'Content-Type': 'application/json',
      },
    });
  } catch (error) {
    throw new Error('Client deletion failed.');
  }
};

/**
 * Fetches clients.
 *
 * @param {PaginatedClientRequestDTO} request - Paginated client get request
 * @returns {Promise<PageableResponse<Client>>} - Promise that resolves to the paginated clients.
 *
 * @throws {Error} - Throws an error if the fetching fails.
 */
export const get = async (
  request: PaginatedClientRequestDTO = {
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    page: 0,
    pageSize: 10,
    uniqueIdentifier: '',
  }
): Promise<PageableResponse<Client>> => {
  try {
    const response = await apiClient<PageableResponse<Client>>(
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
    throw new Error('Client fetching failed.');
  }
};
