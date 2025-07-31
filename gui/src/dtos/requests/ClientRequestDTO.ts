/**
 * Client request DTO.
 *
 * @property {number} id - The ID of the client
 * @property {string} firstName - The first name of the client
 * @property {string} lastName - The last name of the client
 * @property {string} email - The email of the client
 * @property {string} phone - The phone number of the client
 * @property {string} uniqueIdentifier - The unique identifier of the client
 */
export interface ClientRequestDTO {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  uniqueIdentifier: string;
}
