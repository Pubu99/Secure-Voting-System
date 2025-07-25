package com.securevote.secure_voting.model;

import jakarta.persistence.*;

@Entity
@Table(name = "votes")
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String encryptedVote;

    private String hash;

    // Getters and Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getEncryptedVote() { return encryptedVote; }

    public void setEncryptedVote(String encryptedVote) { this.encryptedVote = encryptedVote; }

    public String getHash() { return hash; }

    public void setHash(String hash) { this.hash = hash; }
}
