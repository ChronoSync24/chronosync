package com.sinergy.chronosync.service.impl;

import com.sinergy.chronosync.builder.ClientFilterBuilder;
import com.sinergy.chronosync.dto.request.ClientRequestDTO;
import com.sinergy.chronosync.exception.InvalidStateException;
import com.sinergy.chronosync.exception.RepositoryException;
import com.sinergy.chronosync.model.Client;
import com.sinergy.chronosync.model.Firm;
import com.sinergy.chronosync.repository.ClientRepository;
import com.sinergy.chronosync.service.BaseService;
import com.sinergy.chronosync.service.ClientService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service implementation for managing clients.
 */
@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepository;
	private final BaseService baseService;

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
	public Page<Client> getClients(PageRequest pageRequest) {
		return clientRepository.findAll(ClientFilterBuilder.hasFirm(
			baseService.getAuthUserFirm().getId()), pageRequest);
	}

	/**
	 * Creates a new client or updates an existing to associate it with the current user's firm.
	 *
	 * @param requestDto {@link ClientRequestDTO} containing client details
	 * @return {@link Client} representing the saved client
	 */
	@Override
	@Transactional
	public Client createClient(ClientRequestDTO requestDto) {
		Firm authUserFirm = baseService.getAuthUserFirm();

		Specification<Client> spec = ClientFilterBuilder.builder()
			.firstName(requestDto.getFirstName())
			.lastName(requestDto.getLastName())
			.email(requestDto.getEmail())
			.phone(requestDto.getPhone())
			.build().toSpecification();

		Optional<Client> existingClientOptional = clientRepository.findOne(spec);

		if (existingClientOptional.isPresent()) {
			Client existingClient = existingClientOptional.get();
			if (!existingClient.getFirms().contains(authUserFirm)) {
				existingClient.getFirms().add(authUserFirm);
				authUserFirm.getClients().add(existingClient);
			}
			return clientRepository.update(existingClient);
		} else {
			Client client = requestDto.toModel();
			client.getFirms().add(authUserFirm);
			authUserFirm.getClients().add(client);
			return clientRepository.create(client);
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
		return clientRepository.update(requestDto.toModel());
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
			throw new InvalidStateException("Client not found");
		}
		clientRepository.deleteById(id);
	}
}
