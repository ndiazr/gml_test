package com.gml.clients.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, name = "shared_key")
    private String sharedKey;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String phone;

    @NotBlank
    @Column(nullable = false)
    private String email;

    @NotNull
    @Column(nullable = false, name = "start_date")
    private LocalDate startDate;

    @NotNull
    @Column(nullable = false, name = "end_date")
    private LocalDate endDate;
}
