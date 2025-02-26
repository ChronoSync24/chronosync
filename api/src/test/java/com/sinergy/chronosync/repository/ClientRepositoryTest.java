package com.sinergy.chronosync.repository;

import com.sinergy.chronosync.builder.ClientFilterBuilder;
import com.sinergy.chronosync.dto.request.ClientRequestDTO;
import com.sinergy.chronosync.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ClientRepository}.
 */
class ClientRepositoryTest {

	@Mock
	private ClientRepository clientRepository;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * Tests the {@link ClientRepository#findAll(Specification)} method with a specification filter.
	 * Verifies that the repository returns a list of clients matching the specification.
	 */
	@Test
	void findByNameTest() {
		ClientFilterBuilder filterBuilder = ClientFilterBuilder.builder()
			.firstName("John")
			.build();
		Specification<Client> spec = filterBuilder.toSpecification();

		ClientRequestDTO clientDTO = ClientRequestDTO.builder()
			.firstName("John")
			.lastName("Doe")
			.email("john.doe@example.com")
			.phone("123-456-789")
			.build();

		List<Client> clients = List.of(clientDTO.toModel());

		when(clientRepository.findAll(Mockito.<Specification<Client>>any())).thenReturn(clients);

		List<Client> result = clientRepository.findAll(spec);

		assertThat(result)
			.isNotNull()
			.isNotEmpty()
			.allMatch(c -> "John".equals(c.getFirstName()));

		verify(clientRepository, times(1)).findAll(spec);
	}

	/**
	 * Tests the {@link ClientRepository#findAll(Specification)} method with no matching clients.
	 * Verifies that the repository returns an empty list when no clients match the specification.
	 */
	@Test
	void findWithNoMatchTest() {
		ClientFilterBuilder filterBuilder = ClientFilterBuilder.builder()
			.firstName("Nonexistent")
			.build();
		Specification<Client> spec = filterBuilder.toSpecification();

		when(clientRepository.findAll(Mockito.<Specification<Client>>any())).thenReturn(List.of());

		List<Client> result = clientRepository.findAll(spec);

		assertThat(result).isEmpty();
		verify(clientRepository, times(1)).findAll(spec);
	}

	/**
	 * Tests the {@link ClientRepository#findOne(Specification)} method when a matching client is found.
	 */
	@Test
	void findOneTest() {
		ClientRequestDTO client = ClientRequestDTO.builder()
			.id(1L)
			.firstName("John")
			.lastName("Doe")
			.email("john.doe@example.com")
			.phone("123-456-789")
			.build();

		Client clientModel = client.toModel();

		ClientFilterBuilder filterBuilder = ClientFilterBuilder.builder()
			.firstName("John")
			.build();
		Specification<Client> spec = filterBuilder.toSpecification();

		when(clientRepository.findOne(Mockito.<Specification<Client>>any())).thenReturn(Optional.of(clientModel));

		Optional<Client> result = clientRepository.findOne(spec);

		assertThat(result).isPresent();
		assertEquals(clientModel.getFirstName(), result.get().getFirstName());
		assertEquals(clientModel.getLastName(), result.get().getLastName());
		assertEquals(clientModel.getId(), result.get().getId());

		verify(clientRepository, times(1)).findOne(spec);
	}

	/**
	 * Tests the {@link ClientRepository#findAll()} method.
	 * Verifies that the repository returns a page of clients.
	 */
	@Test
	void findAllPaginatedTest() {
		Client client = new Client();
		client.setId(1L);
		client.setFirstName("John");
		client.setLastName("Doe");
		client.setEmail("john.doe@example.com");

		Page<Client> page = new PageImpl<>(List.of(client), PageRequest.of(0, 10), 1);

		when(clientRepository.findAll(Mockito.any(PageRequest.class))).thenReturn(page);

		Page<Client> result = clientRepository.findAll(PageRequest.of(0, 10));

		assertThat(result).isNotNull();
		assertThat(result.getTotalElements()).isEqualTo(1);
		assertThat(result.getContent()).contains(client);

		verify(clientRepository, times(1)).findAll(Mockito.any(PageRequest.class));
	}
}
