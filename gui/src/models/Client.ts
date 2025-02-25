import { BaseEntity } from './BaseEntity';
import { Firm } from './Firm';

/**
 * Client model interface.
 */
export interface Client extends BaseEntity {
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    firm?: Firm;
}
