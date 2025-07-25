package com.securevote.secure_voting.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
}