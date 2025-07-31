import { PageableRequest } from '../../models/BaseEntity';

/**
 * Paginated client request DTO for search operations.
 */
export interface PaginatedClientRequestDTO extends PageableRequest {
  firstName?: string;
  lastName?: string;
  email?: string;
  phone?: string;
  uniqueIdentifier?: string;
}
