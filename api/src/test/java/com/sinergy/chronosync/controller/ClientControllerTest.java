package com.sinergy.chronosync.controller;

import com.sinergy.chronosync.dto.request.BasePaginationRequest;
import com.sinergy.chronosync.dto.request.ClientRequestDTO;
import com.sinergy.chronosync.model.Client;
import com.sinergy.chronosync.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ClientController}.
 */
class ClientControllerTest {

	@Mock
	private ClientServiceImpl clientService;

	@InjectMocks
	private ClientController clientController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * Tests the {@link ClientController#getClients(BasePaginationRequest)} method.
	 * Verifies that the service is called with the correct parameters and the response is valid.
	 */
	@Test
	void getClientsTest() {
		int page = 0;
		int size = 10;

		Client client = Client.builder()
			.firstName("John")
			.lastName("Doe")
			.email("john.doe@example.com")
			.build();
		Page<Client> mockPage = new PageImpl<>(List.of(client), PageRequest.of(page, size), 1);

		BasePaginationRequest paginationRequest = new BasePaginationRequest();
		paginationRequest.setPage(page);
		paginationRequest.setPageSize(size);

		PageRequest pageRequest = PageRequest.of(page, size);

		when(clientService.getClients(pageRequest)).thenReturn(mockPage);

		ResponseEntity<Page<Client>> response = clientController.getClients(paginationRequest);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getContent()).contains(client);

		verify(clientService, times(1)).getClients(pageRequest);
	}

	/**
	 * Tests the {@link ClientController#createClient(ClientRequestDTO)} method.
	 * Verifies that the service is called with the correct DTO and the response contains the created entity.
	 */
	@Test
	void createClientTest() {
		ClientRequestDTO requestDTO = ClientRequestDTO.builder()
			.firstName("John")
			.lastName("Doe")
			.email("john.doe@example.com")
			.phone("123-456-7890")
			.build();

		Client createdClient = Client.builder()
			.firstName("John")
			.lastName("Doe")
			.email("john.doe@example.com")
			.build();

		when(clientService.createClient(requestDTO)).thenReturn(createdClient);

		ResponseEntity<Client> response = clientController.createClient(requestDTO);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody()).isEqualTo(createdClient);

		verify(clientService, times(1)).createClient(requestDTO);
	}

	/**
	 * Tests the {@link ClientController#updateClient(ClientRequestDTO)} method.
	 * Verifies that the service is called with the correct DTO and the response contains the updated entity.
	 */
	@Test
	void updateClientTest() {
		Client existingClient = new Client();
		existingClient.setId(1L);
		existingClient.setFirstName("John");
		existingClient.setLastName("Doe");
		existingClient.setEmail("john.doe@example.com");
		existingClient.setPhone("123-456-7890");

		ClientRequestDTO requestDTO = ClientRequestDTO.builder()
			.firstName("John")
			.lastName("Doo")
			.email("john.doe@example.com")
			.phone("123-456-7890")
			.build();

		Client updatedClient = requestDTO.toModel();
		updatedClient.setId(existingClient.getId());

		when(clientService.updateClient(requestDTO)).thenReturn(updatedClient);

		ResponseEntity<Client> response = clientController.updateClient(requestDTO);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getId()).isEqualTo(existingClient.getId());
		assertThat(response.getBody().getLastName()).isEqualTo("Doo");

		verify(clientService, times(1)).updateClient(requestDTO);
	}

	/**
	 * Tests the {@link ClientController#deleteClient(Long)} method.
	 * Verifies that the service is called with the correct ID and the response status is 204 (No Content).
	 */
	@Test
	void deleteClientTest() {
		Long id = 1L;

		doNothing().when(clientService).deleteClient(id);

		ResponseEntity<Client> response = clientController.deleteClient(id);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		verify(clientService, times(1)).deleteClient(id);
	}
}
