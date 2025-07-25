package com.securevote.secure_voting.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    private String role;

    @Column(name = "has_voted", nullable = false)
    private boolean hasVoted;

    // Getters and Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }

    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public boolean isHasVoted() { return hasVoted; }

    public void setHasVoted(boolean hasVoted) { this.hasVoted = hasVoted; }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }
}
