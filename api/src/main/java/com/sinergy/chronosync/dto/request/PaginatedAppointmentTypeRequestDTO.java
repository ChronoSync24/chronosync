package com.sinergy.chronosync.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Paginated appointment type request DTO.
 */
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class PaginatedAppointmentTypeRequestDTO extends BasePaginationRequest {
	private String name;
}
