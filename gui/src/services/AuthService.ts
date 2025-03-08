import { LoginRequestDTO } from '../dtos/requests/LoginRequestDTO';
import { apiClient } from '../utils/ApiClient';

const ENDPOINT_PREFIX = '/auth';

//TODO: Setting token and removing token from the local storage does not belong to the service. Return the jwt string, and move logic to login tsx file. Change docs and return of the method.

/**
 * Logs in user with the provided credentials.
 *
 * @param {LoginRequestDTO} request - The login credentials (username and password).
 * @returns {Promise<string>} - user's JWT
 */
export const login = async (request: LoginRequestDTO): Promise<string> => {
  const response = await apiClient<{ jwtString: string }>(`${ENDPOINT_PREFIX}/login`, {
    method: 'POST',
    body: request,
  });

  return response.jwtString;
};

/**
 * Logs out user.
 */
export const logout = async (): Promise<void> => {
  await apiClient<void>(`${ENDPOINT_PREFIX}/logout`, {
    method: 'GET',
    headers: {
      Authorization: 'Bearer ' + localStorage.getItem('token'),
      'Content-Type': 'application/json',
    },
  });
};

/**
 * Validates JWT from the header.
 *
 * @returns {Promise<Boolean>} - validity of JWT
 */
export const validateToken = async (): Promise<Boolean> => {
  const response = await apiClient<Boolean>(`${ENDPOINT_PREFIX}/validate-token`, {
    method: 'GET',
    headers: {
      Authorization: 'Bearer ' + localStorage.getItem('token'),
      'Content-Type': 'application/json',
    },
  });

  return response;
};
