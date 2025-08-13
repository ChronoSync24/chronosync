package com.sinergy.chronosync.service.impl;

import com.sinergy.chronosync.builder.ClientFilterBuilder;
import com.sinergy.chronosync.dto.request.ClientRequestDTO;
import com.sinergy.chronosync.dto.request.PaginatedClientRequestDTO;
import com.sinergy.chronosync.exception.EntityNotFoundException;
import com.sinergy.chronosync.exception.InvalidStateException;
import com.sinergy.chronosync.exception.RepositoryException;
import com.sinergy.chronosync.model.Client;
import com.sinergy.chronosync.repository.ClientRepository;
import com.sinergy.chronosync.service.ClientService;
import com.sinergy.chronosync.service.SecurityContextService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing clients.
 */
@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepository;
	private final SecurityContextService securityContextService;

	/**
	 * Retrieves all clients associated with the current user's firm.
	 * <p>
	 * This method checks the current logged-in user's firm and returns
	 * a list of {@link Client} objects linked to that firm's ID.
	 *
	 * @param pageRequest The pagination and sorting information
	 * @return {@link Page} clients associated with the authenticated user's firm
	 */
	@Override
	public Page<Client> getClients(PaginatedClientRequestDTO pageRequest) {
		ClientFilterBuilder filterBuilder = ClientFilterBuilder.builder()
			.firmId(securityContextService.getAuthUserFirm().getId())
			.firstName(pageRequest.getFirstName())
			.lastName(pageRequest.getLastName())
			.email(pageRequest.getEmail())
			.phone(pageRequest.getPhone())
			.uniqueIdentifier(pageRequest.getUniqueIdentifier())
			.build();

		filterBuilder.setPageable(PageRequest.of(pageRequest.getPage(), pageRequest.getPageSize()));

		return clientRepository.findAll(filterBuilder.toSpecification(), filterBuilder.getPageable());
	}

	/**
	 * Creates a new client associated with the current user's firm.
	 *
	 * @param requestDto {@link ClientRequestDTO} containing client details
	 * @return {@link Client} representing the saved client
	 */
	@Override
	@Transactional
	public Client createClient(ClientRequestDTO requestDto) {
		Client client = requestDto.toModel(securityContextService.getAuthUserFirm());
		client.setFirm(securityContextService.getAuthUserFirm());
		try {
			return clientRepository.create(client);
		} catch (DataIntegrityViolationException e) {
			throw new RepositoryException("A client with the same details already exists for this firm.");
		}
	}

	/**
	 * Updates an existing client.
	 *
	 * @param requestDto {@link ClientRequestDTO} containing client details
	 * @return {@link Client} representing the updated or newly created client
	 * @throws RepositoryException if the client cannot be found for update
	 */
	@Override
	public Client updateClient(ClientRequestDTO requestDto) {
		return clientRepository.update(requestDto.toModel(securityContextService.getAuthUserFirm()));
	}

	/**
	 * Deletes a client identified by its ID.
	 *
	 * @param id {@link Long} ID of the appointment type to delete
	 * @throws InvalidStateException if deletion fails or the client does not exist
	 */
	@Override
	public void deleteClient(Long id) {
		if (!clientRepository.existsById(id)) {
			throw new EntityNotFoundException("Client not found");
		}
		clientRepository.deleteById(id);
	}
}
