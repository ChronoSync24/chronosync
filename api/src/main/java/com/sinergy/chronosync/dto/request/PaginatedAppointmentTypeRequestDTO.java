package com.sinergy.chronosync.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class PaginatedAppointmentTypeRequestDTO extends BasePaginationRequest {
	private String name;
}
