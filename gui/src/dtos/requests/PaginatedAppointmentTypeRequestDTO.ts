import { PageableRequest } from '../../models/BaseEntity';

/**
 * Paginated appointment type request DTO.
 */
export interface PaginatedAppointmentTypeRequestDTO extends PageableRequest {
  name: string;
}
