package com.sinergy.chronosync.service;

import com.sinergy.chronosync.dto.request.ClientRequestDTO;
import com.sinergy.chronosync.dto.request.PaginatedClientRequestDTO;
import com.sinergy.chronosync.exception.EntityNotFoundException;
import com.sinergy.chronosync.exception.RepositoryException;
import com.sinergy.chronosync.model.Client;
import com.sinergy.chronosync.model.Firm;
import com.sinergy.chronosync.model.user.User;
import com.sinergy.chronosync.repository.ClientRepository;
import com.sinergy.chronosync.repository.UserRepository;
import com.sinergy.chronosync.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceTest {

	@Mock
	private ClientRepository clientRepository;

	@Mock
	private SecurityContextService securityContextService;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private ClientServiceImpl clientService;

	/**
	 * Sets up Mock dependencies before each test.
	 */
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		SecurityContext securityContext = mock(SecurityContext.class);
		Authentication authentication = mock(Authentication.class);

		when(authentication.getName()).thenReturn("testUser");
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		Firm firm = new Firm();
		firm.setId(1L);
		when(securityContextService.getAuthUserFirm()).thenReturn(firm);

		User user = new User();
		user.setUsername("testUser");
		user.setFirm(firm);

		when(userRepository.findOne(Mockito.<Specification<User>>any())).thenReturn(Optional.of(user));
	}

	/**
	 * Tests the getClients method when the request is valid.
	 */
	@Test
	void getClientsTest() {
		PaginatedClientRequestDTO paginatedRequest = new PaginatedClientRequestDTO();
		ClientRequestDTO newClient = ClientRequestDTO.builder()
			.firstName("John")
			.lastName("Doe")
			.email("john@doe.com")
			.phone("123-456-789")
			.build();

		Firm firm = new Firm();
		firm.setId(1L);

		Page<Client> clients = new PageImpl<>(List.of(newClient.toModel(firm)));

		when(clientRepository.findAll(Mockito.<Specification<Client>>any(), any(Pageable.class))).thenReturn(clients);

		Page<Client> result = clientService.getClients(paginatedRequest);

		assertNotNull(result, "Result should not be null");
		assertEquals(1, result.getTotalElements(), "Should contain 1 client");
		assertEquals("John", result.getContent().getFirst().getFirstName());

		verify(clientRepository, times(1)).findAll(
			Mockito.<Specification<Client>>any(),
			any(Pageable.class)
		);
	}

	/**
	 * Tests the createClient method when the request is valid.
	 */
	@Test
	void createClientTest() {
		Firm firm = new Firm();
		firm.setId(1L);

		User user = new User();
		user.setFirm(firm);

		ClientRequestDTO requestDto = ClientRequestDTO.builder()
			.firstName("John")
			.lastName("Doe")
			.email("john.doe@example.com")
			.phone("123456789")
			.build();

		Client client = requestDto.toModel(firm);
		client.setId(1L);

		when(userRepository.findOne(Mockito.<Specification<User>>any())).thenReturn(Optional.of(user));

		when(clientRepository.create(any(Client.class))).thenReturn(client);

		Client createdClient = clientService.createClient(requestDto);

		assertNotNull(createdClient);
		assertEquals("John", createdClient.getFirstName());
		assertEquals("Doe", createdClient.getLastName());
		assertEquals("john.doe@example.com", createdClient.getEmail());

		verify(clientRepository, times(1)).create(any(Client.class));
	}

	/**
	 * Tests the updateClient method.
	 */
	@Test
	void updateClientTest() {
		ClientRequestDTO requestDto = ClientRequestDTO.builder()
			.firstName("Jane")
			.lastName("Doe")
			.email("jane.doe@example.com")
			.phone("987654321")
			.build();

		when(clientRepository.update(any(Client.class)))
			.thenAnswer(invocation -> invocation.getArgument(0));

		Client updatedClient = clientService.updateClient(requestDto);

		assertNotNull(updatedClient);
		assertEquals(requestDto.getFirstName(), updatedClient.getFirstName());
		assertEquals(requestDto.getLastName(), updatedClient.getLastName());
		assertEquals(requestDto.getEmail(), updatedClient.getEmail());

		verify(clientRepository, times(1)).update(any(Client.class));
	}

	/**
	 * Tests the updateClient method when the client is not found.
	 */
	@Test
	void updateClientNotFoundTest() {
		ClientRequestDTO requestDto = ClientRequestDTO.builder()
			.firstName("Jane")
			.lastName("Doe")
			.email("john.doe@example.com")
			.phone("987654321")
			.build();

		when(clientRepository.update(any(Client.class))).thenThrow(RepositoryException.class);

		assertThrows(RepositoryException.class, () -> clientService.updateClient(requestDto));

		verify(clientRepository, times(1)).update(any(Client.class));
	}

	/**
	 * Tests the deleteClient method when the client exists.
	 */
	@Test
	void deleteClientTest() {
		Long clientId = 1L;
		when(clientRepository.existsById(clientId)).thenReturn(true);
		clientService.deleteClient(clientId);
		verify(clientRepository, times(1)).deleteById(clientId);
	}

	/**
	 * Tests the deleteClient method when the client doesn't exist.
	 */
	@Test
	void deleteClientNotFoundTest() {
		Long clientId = 1L;
		when(clientRepository.existsById(clientId)).thenReturn(false);

		EntityNotFoundException thrownException = assertThrows(EntityNotFoundException.class, () ->
			clientService.deleteClient(clientId));

		assertEquals("Client not found", thrownException.getMessage());
		verify(clientRepository, never()).deleteById(clientId);
	}
}
