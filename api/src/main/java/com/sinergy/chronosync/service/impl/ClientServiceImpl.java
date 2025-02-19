package com.sinergy.chronosync.service.impl;

import com.sinergy.chronosync.builder.ClientFilterBuilder;
import com.sinergy.chronosync.builder.UserFilterBuilder;
import com.sinergy.chronosync.dto.request.ClientRequestDTO;
import com.sinergy.chronosync.exception.InvalidStateException;
import com.sinergy.chronosync.exception.UserNotFoundException;
import com.sinergy.chronosync.model.Client;
import com.sinergy.chronosync.model.Firm;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.repository.ClientRepository;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.service.ClientService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service implementation for managing clients.
 *
 * <p>This service handles all business logic related to clients.</p>
 */
@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepository;
	private final UserRepository userRepository;

	/**
	 * Retrieves all clients associated with the current user's firm.
	 * <p>
	 * This method checks the current logged-in user's firm and returns
	 * a list of {@link Client} objects linked to that firm's ID.
	 *
	 * @param pageRequest The pagination and sorting information
	 * @return {@link Page} of {@link Client} objects associated with the current user's firm
	 */
	@Override
	public Page<Client> getClients(PageRequest pageRequest) {
		Long firmId = getAuthUserFirm().getId();
		return clientRepository.findAll(ClientFilterBuilder.hasFirm(firmId), pageRequest);
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
		Firm authUserFirm = getAuthUserFirm();

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
			return clientRepository.update(existingClient, existingClient.getId());
		} else {
			Client client = requestDto.toModel();
			client.getFirms().add(authUserFirm);
			authUserFirm.getClients().add(client);
			return clientRepository.create(client);
		}
	}

	/**
	 * Updates an existing client or throws an exception if the client is not found.
	 *
	 * @param requestDto {@link ClientRequestDTO} containing client details
	 * @return {@link Client} representing the updated or newly created client
	 * @throws InvalidStateException if the client cannot be found for update
	 */
	@Override
	public Client updateClient(ClientRequestDTO requestDto){

        Client existingClient = clientRepository.findById(requestDto.getId())
                .orElseThrow(
                        () -> new InvalidStateException("Client not found")
                );

		existingClient.setFirstName(requestDto.getFirstName());
		existingClient.setLastName(requestDto.getLastName());
		existingClient.setEmail(requestDto.getEmail());
		existingClient.setPhone(requestDto.getPhone());

		return clientRepository.update(existingClient, requestDto.getId());
	}

	/**
	 * Deletes a client identified by its ID.
	 *
	 * @param id {@link Long} ID of the appointment type to delete
	 * @throws InvalidStateException if deletion fails or the client does not exist
	 */
	@Override
	public void deleteClient(Long id){
		if (!clientRepository.existsById(id)) {
			throw new InvalidStateException("Client not found");
		}
		clientRepository.deleteById(id);
	}

	/**
	 * Retrieves the authenticated user's associated firm.
	 *
	 * <p>This method extracts the currently authenticated username from the security context
	 * and constructs a filter to query the user repository. If the user is found, their associated
	 * firm is returned. If no user is found, a {@link UserNotFoundException} is thrown. If the user
	 * is not linked to a firm, an {@link InvalidStateException} is thrown.</p>
	 *
	 * @return the {@link Firm} associated with the authenticated user
	 * @throws UserNotFoundException if no user is found with the authenticated username
	 * @throws InvalidStateException if the user is not associated with any firm
	 */
	private Firm getAuthUserFirm() {
		UserFilterBuilder filterBuilder = UserFilterBuilder.builder()
			.username(SecurityContextHolder.getContext().getAuthentication().getName())
			.build();

		User user = userRepository.findOne(filterBuilder.toSpecification())
			.orElseThrow(() -> new UserNotFoundException("User not found"));

		Firm firm = user.getFirm();
		if (firm == null) {
			throw new InvalidStateException("User is not associated with any firm.");
		}

		return firm;
	}
}
