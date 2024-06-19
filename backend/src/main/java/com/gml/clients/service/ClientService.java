package com.gml.clients.service;

import com.gml.clients.entity.Client;
import com.gml.clients.exception.ResourceNotFoundException;
import com.gml.clients.repository.ClientRepository;
import com.gml.clients.util.CsvGenerator;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public List<Client> getAll() {
        return repository.findAll();
    }

    public Client getById(Long clientId) {
        return repository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Client with id %x not found", clientId)));
    }

    public List<Client> getBySharedKey(String sharedKey) {
        return repository.findBySharedKeyContaining(sharedKey);
    }

    public Client create(Client client) {
        return repository.save(client);
    }

    public Client update(Client client) {
        Optional<Client> existingClient = repository.findById(client.getId());
        if (existingClient.isPresent()) {
            Client clientObj = existingClient.get();
            clientObj.setSharedKey(client.getSharedKey());
            clientObj.setName(client.getName());
            clientObj.setEmail(client.getEmail());
            clientObj.setPhone(client.getPhone());
            clientObj.setStartDate(client.getStartDate());
            clientObj.setEndDate(client.getEndDate());
            return repository.save(clientObj);
        } else {
            throw new ResourceNotFoundException(String.format("Client with id %x not found", client.getId()));
        }
    }

    public void delete(Long clientId) {
        repository.deleteById(clientId);
    }

    public ByteArrayInputStream generateCsv() {
        return CsvGenerator.clientsToCSV(repository.findAll());
    }
}
