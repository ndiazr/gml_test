package com.gml.clients.controller;

import com.gml.clients.entity.Client;
import com.gml.clients.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("clients")
public class ClientController {
    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAll() {
        List<Client> clients = service.getAll();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("{id}")
    public ResponseEntity<Client> getById(@PathVariable("id") Long clientId) {
        Client client = service.getById(clientId);
        return ResponseEntity.ok(client);
    }

    @GetMapping("{sharedKey}/sharedKey")
    public ResponseEntity<List<Client>> getBySharedKey(@PathVariable("sharedKey") String sharedKey) {
        System.out.println(sharedKey);
        List<Client> clients = service.getBySharedKey(sharedKey);
        return ResponseEntity.ok(clients);
    }

    @PostMapping
    public ResponseEntity<Client> create(@Valid @RequestBody Client client) {
        Client savedAccount = service.create(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
    }

    @PutMapping("{id}")
    public ResponseEntity<Client> update(@PathVariable("id") Long clientId, @Valid @RequestBody Client client) {
        client.setId(clientId);
        Client updatedAccount = service.update(client);
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long clientId) {
        service.delete(clientId);
        return ResponseEntity.noContent().build();
    }
}
