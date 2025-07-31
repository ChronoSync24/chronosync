/**
 * Base entity interface with common fields.
 *
 * @property {number} id - The ID of the entity
 */
export interface BaseEntity {
  id: number;
}

/**
 * Base pageable request interface for pagination parameters.
 *
 * @property {number} [page] - The page number (default: 0)
 * @property {number} [pageSize] - The number of items per page (default: 10)
 */
export interface PageableRequest {
  page?: number;
  pageSize?: number;
}

/**
 * Base pageable response interface for paginated data.
 *
 * @property {T[]} content - The content of the page
 * @property {number} totalElements - The total number of elements
 * @property {number} totalPages - The total number of pages
 * @property {number} number - The page number
 * @property {number} size - The number of items per page
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
