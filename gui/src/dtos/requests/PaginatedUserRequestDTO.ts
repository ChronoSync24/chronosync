import { PageableRequest } from '../../models/BaseEntity';
import { UserRole } from '../../models/user/UserRole';

/**
 * Paginated user request DTO for search operations.
 */
export interface PaginatedUserRequestDTO extends PageableRequest {
  firstName?: string;
  lastName?: string;
  email?: string;
  phone?: string;
  uniqueIdentifier?: string;
  roles?: UserRole[];
}
