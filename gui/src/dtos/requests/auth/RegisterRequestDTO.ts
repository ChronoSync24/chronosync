import { UserRole } from '../../../models/user/UserRole';

/**
 * Register request DTO.
 */
export interface RegisterRequestDTO {
	firstName: string;
	lastName: string;
	identificationNumber: string;
	address: string;
	phone: string;
	email: string;
	password: string;
	role: UserRole;
}
