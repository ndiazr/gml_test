package com.gml.clients.repository;

import com.gml.clients.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findBySharedKeyContaining(String sharedKey);
}
