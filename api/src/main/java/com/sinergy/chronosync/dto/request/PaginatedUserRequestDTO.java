package com.sinergy.chronosync.dto.request;

import com.sinergy.chronosync.model.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Paginated user request DTO.
 */
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class PaginatedUserRequestDTO extends BasePaginationRequest {

	private String firstName;
	private String lastName;
	private String uniqueIdentifier;
	private String username;
	private List<UserRole> roles;
	private Long firmId;
}
