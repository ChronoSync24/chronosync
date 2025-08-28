import { apiClient } from '../utils/ApiClient';
import { UserRequestDTO } from '../dtos/requests/UserRequestDTO';
import { User } from '../models/user/User';
import { PaginatedUserRequestDTO } from '../dtos/requests/PaginatedUserRequestDTO';
import { PageableResponse } from '../models/BaseEntity';

const ENDPOINT_PREFIX = '/user';

/**
 * Creates a new user.
 *
 * @param {UserRequestDTO} request - User creation request
 * @returns {Promise<User>} - Promise that resolves to the created user object.
 *
 * @throws {Error} - Throws an error if the creation fails.
 */
export const create = async (request: UserRequestDTO): Promise<User> => {
  try {
    const response = await apiClient<User>(`${ENDPOINT_PREFIX}/create`, {
      method: 'POST',
      body: request,
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('token'),
        'Content-Type': 'application/json',
      },
    });

    return response;
  } catch (error) {
    throw new Error('User creation failed.');
  }
};

/**
 * Updates a user.
 *
 * @param {UserRequestDTO} request - User update request
 * @returns {Promise<User>} - Promise that resolves to the updated user.
 *
 * @throws {Error} - Throws an error if the update fails.
 */
export const update = async (request: UserRequestDTO): Promise<User> => {
  try {
    const response = await apiClient<User>(`${ENDPOINT_PREFIX}`, {
      method: 'PUT',
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('token'),
        'Content-Type': 'application/json',
      },
      body: request,
    });

    return response;
  } catch (error) {
    throw new Error('User update failed.');
  }
};

/**
 * Deletes a user.
 *
 * @param {number} id - User id
 *
 * @throws {Error} - Throws an error if the deletion fails
 */
export const remove = async (id: number): Promise<void> => {
  try {
    await apiClient<User>(`${ENDPOINT_PREFIX}?id=${id}`, {
      method: 'DELETE',
      headers: {
        Authorization: 'Bearer ' + localStorage.getItem('token'),
        'Content-Type': 'application/json',
      },
    });
  } catch (error) {
    throw new Error('User deletion failed.');
  }
};

/**
 * Fetches users.
 *
 * @param {PaginatedUserRequestDTO} request - Paginated user get request
 * @returns {Promise<PageableResponse<User>>} - Promise that resolves to the paginated users.
 *
 * @throws {Error} - Throws an error if the fetching fails.
 */
export const get = async (
  request: PaginatedUserRequestDTO = {
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    page: 0,
    pageSize: 10,
    uniqueIdentifier: '',
    roles: [],
  }
): Promise<PageableResponse<User>> => {
  try {
    const response = await apiClient<PageableResponse<User>>(
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
    throw new Error('User fetching failed.');
  }
};
