package com.sinergy.chronosync.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Paginated client request DTO.
 */
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class PaginatedClientRequestDTO extends BasePaginationRequest {
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String uniqueIdentifier;
}
