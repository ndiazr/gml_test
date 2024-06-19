package com.gml.clients.service;

import com.gml.clients.entity.Client;
import com.gml.clients.exception.ResourceNotFoundException;
import com.gml.clients.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    public void getAllTest() {
        List<Client> clientList = Arrays.asList(
                new Client(1L, "shKey1", "name", "email", "phone", LocalDate.now(), LocalDate.now()),
                new Client(2L, "shKey1", "name", "email", "phone", LocalDate.now(), LocalDate.now()));

        when(clientRepository.findAll())
                .thenReturn(clientList);

        List<Client> response = clientService.getAll();

        assertThat(response).isNotEmpty();
        assertThat(response.get(1).getId()).isEqualTo(2L);
        verify(clientRepository).findAll();
    }

    @Test
    public void getByIdTest() {
        when(clientRepository.findById(anyLong())).thenReturn(
                Optional.of(new Client(
                        1L,
                        "shKey1",
                        "name",
                        "email",
                        "phone",
                        LocalDate.now(),
                        LocalDate.now())));

        Client response = clientService.getById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getSharedKey()).isEqualTo("shKey1");
        verify(clientRepository).findById(any(Long.class));
    }

    @Test
    public void getByIdExceptionTest() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());

        try {
            Client response = clientService.getById(1L);
        } catch (Exception e) {
            assertThat(e.getMessage()).contains("Client with id");
        }
        verify(clientRepository).findById(any(Long.class));
    }

    @Test
    public void getBySharedKeyTest() {
        when(clientRepository.findBySharedKeyContaining("shKey1")).thenReturn(
                List.of(new Client(
                        1L,
                        "shKey1",
                        "name",
                        "email",
                        "phone",
                        LocalDate.now(),
                        LocalDate.now())));

        assertThat(clientService.getBySharedKey("shKey1")).isNotEmpty();
        assertThat(clientService.getBySharedKey("shKey2")).isEmpty();
        verify(clientRepository, times(2)).findBySharedKeyContaining(anyString());
    }

    @Test
    public void createTest() {
        when(clientRepository.save(any(Client.class))).thenReturn(
                new Client(
                        1L,
                        "shKey1",
                        "name",
                        "email",
                        "phone",
                        LocalDate.now(),
                        LocalDate.now()));

        assertThat(clientService.create(new Client())).isNotNull();
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    public void updateTest() {
        Client client = new Client(
                1L,
                "shKey1",
                "name",
                "email",
                "phone",
                LocalDate.now(),
                LocalDate.now());

        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(new Client()));
        when(clientRepository.save(any(Client.class))).thenReturn(new Client());

        assertThat(clientService.update(client)).isNotNull();
        verify(clientRepository).findById(anyLong());
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    public void deleteTest() {
        clientService.delete(1L);
        verify(clientRepository).deleteById(1L);
    }
}
