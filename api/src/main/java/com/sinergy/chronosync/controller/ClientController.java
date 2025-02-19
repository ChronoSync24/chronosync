package com.sinergy.chronosync.controller;

import com.sinergy.chronosync.dto.request.BasePaginationRequest;
import com.sinergy.chronosync.dto.request.ClientRequestDTO;
import com.sinergy.chronosync.model.Client;
import com.sinergy.chronosync.service.impl.ClientServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing clients.
 */
@RestController
@RequestMapping(path = "api/v1/client")
@RequiredArgsConstructor
public class ClientController {

	private final ClientServiceImpl clientService;

	/**
	 * Retrieves a paginated list of clients associated with the current user's firm.
	 *
	 * <p>This endpoint allows users associated with a firm to view clients
	 * specific to their firm. It ensures data isolation by returning only the clients
	 * linked to the logged-in user's firm.</p>
	 *
	 * <p>The pagination details, such as the page number and page size, are provided
	 * in the request body using the {@link BasePaginationRequest} class. Default values
	 * are used if not specified.</p>
	 */
	@PostMapping("/search")
	public ResponseEntity<Page<Client>> getClients(
		@RequestBody BasePaginationRequest paginationRequest
	) {
		PageRequest pageRequest = PageRequest.of(paginationRequest.getPage(), paginationRequest.getPageSize());
		Page<Client> clients = clientService.getClients(pageRequest);
		return ResponseEntity.ok(clients);
	}

	/**
	 * Creates new client.
	 *
	 * @param request {@link ClientRequestDTO} containing the details of the new client
	 * @return created {@link Client} along with an HTTP status of 201 (Created)
	 */
	@PostMapping("/create")
	public ResponseEntity<Client> createClient(
		@RequestBody ClientRequestDTO request
	) {
		Client client = clientService.createClient(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(client);
	}

	/**
	 * Updates an existing client identified by its ID, or throws an exception if the client does not exist.
	 *
	 * @param request {@link ClientRequestDTO} containing details of the client
	 * @return {@link ResponseEntity} containing the updated or created {@link Client}
	 */
	@PutMapping
	public ResponseEntity<Client> updateClient(
		@RequestBody ClientRequestDTO request
	) {
		Client updatedClient = clientService.updateClient(request);
		return ResponseEntity.ok(updatedClient);
	}

	/**
	 * Deletes a client by its ID.
	 *
	 * @param id {@link Long} ID of the client to delete
	 */
	@DeleteMapping
	public ResponseEntity<Client> deleteClient(
		@RequestParam Long id
	) {
		clientService.deleteClient(id);
		return ResponseEntity.noContent().build();
	}
}
