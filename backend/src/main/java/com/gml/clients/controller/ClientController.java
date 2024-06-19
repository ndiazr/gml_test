package com.gml.clients.controller;

import com.gml.clients.entity.Client;
import com.gml.clients.service.ClientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("clients")
public class ClientController {
    private final ClientService service;
    private final Logger logger = LoggerFactory.getLogger(ClientController.class);

    public ClientController(ClientService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAll() {
        logger.info("Request getAll received");
        List<Client> clients = service.getAll();
        logger.info("Response getAll -> {}", clients);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("{id}")
    public ResponseEntity<Client> getById(@PathVariable("id") Long clientId) {
        logger.info("Request getById received -> id: {}", clientId);
        Client client = service.getById(clientId);
        logger.info("Response getById -> {}", client);
        return ResponseEntity.ok(client);
    }

    @GetMapping("{sharedKey}/sharedKey")
    public ResponseEntity<List<Client>> getBySharedKey(@PathVariable("sharedKey") String sharedKey) {
        logger.info("Request getBySharedKey received -> sharedKey: {}", sharedKey);
        List<Client> clients = service.getBySharedKey(sharedKey);
        logger.info("Response getBySharedKey -> {}", clients);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("csv")
    public ResponseEntity<Resource> generateCsv() {
        logger.info("Request generateCsv received");
        String filename = "clients.csv";
        InputStreamResource file = new InputStreamResource(service.generateCsv());
        logger.info("Response generateCsv generated correctly");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }

    @PostMapping
    public ResponseEntity<Client> create(@Valid @RequestBody Client client) {
        logger.info("Request create received -> payload: {}", client);
        Client savedClient = service.create(client);
        logger.info("Response create -> {}", savedClient);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
    }

    @PutMapping("{id}")
    public ResponseEntity<Client> update(@PathVariable("id") Long clientId, @Valid @RequestBody Client client) {
        logger.info("Request update received -> id: {}, payload: {}", clientId, client);
        client.setId(clientId);
        Client updatedClient = service.update(client);
        logger.info("Response update -> {}", updatedClient);
        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long clientId) {
        logger.info("Request delete received -> id: {}", clientId);
        service.delete(clientId);
        logger.info("Response delete was successfully");
        return ResponseEntity.noContent().build();
    }
}
