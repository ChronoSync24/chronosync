/**
 * Base entity interface with common fields.
 */
export interface BaseEntity {
  id: number;
}

/**
 * Base pageable request interface for pagination parameters.
 */
export interface PageableRequest {
  page?: number;
  pageSize?: number;
}

/**
 * Base pageable response interface for paginated data.
 */
export interface PageableResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}
