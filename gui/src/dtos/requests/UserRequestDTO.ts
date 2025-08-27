import { UserRole } from '../../models/user/UserRole';

/**
 * User create/update request DTO.
 */
export interface UserRequestDTO {
  id: number | null;
  firstName: string;
  lastName: string;
  uniqueIdentifier: string;
  address: string;
  phone: string;
  email: string;
  password?: string;
  role?: UserRole;
  isEnabled: boolean;
}
