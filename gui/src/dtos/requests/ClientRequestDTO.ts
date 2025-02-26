import { Firm } from '../../models/Firm';

/**
 * Client request DTO.
 */
export interface ClientRequestDTO {
    id: number;
    firstName: string;
    lastName: string;
    email: string;
    phone: string;
    firm: Firm;
}
