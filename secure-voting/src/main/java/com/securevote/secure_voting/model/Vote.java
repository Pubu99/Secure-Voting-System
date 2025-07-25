package com.securevote.secure_voting.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "votes")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "encrypted_vote", nullable = false, columnDefinition = "TEXT")
    private String encryptedVote;

    @Column(name = "vote_hash", nullable = false, unique = true)
    private String hash;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    // Getters and Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getEncryptedVote() { return encryptedVote; }

    public void setEncryptedVote(String encryptedVote) { this.encryptedVote = encryptedVote; }

    public String getHash() { return hash; }

    public void setHash(String hash) { this.hash = hash; }

    public LocalDateTime getTimestamp() { return timestamp; }

    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
