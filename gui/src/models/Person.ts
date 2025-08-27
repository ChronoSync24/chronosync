import { BaseEntity } from './BaseEntity';

/**
 * Person interface.
 */
export interface Person extends BaseEntity {
  firstName: string;
  lastName: string;
  uniqueIdentifier: string;
  address: string;
  phone: string;
  email: string;
}
