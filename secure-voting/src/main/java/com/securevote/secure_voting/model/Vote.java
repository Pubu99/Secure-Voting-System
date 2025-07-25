package com.securevote.secure_voting.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String encryptedVote;

    @Column(unique = true, nullable = false)
    private String hash;

    private LocalDateTime timestamp;
}