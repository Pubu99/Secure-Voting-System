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

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String role = "USER"; // Default role

    @Column(name = "has_voted", nullable = false)
    private boolean hasVoted = false; // Default false

    @Column(name = "jwt_token", length = 1000)
    private String jwtToken;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt = new java.util.Date();

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

    public String getJwtToken() { return jwtToken; }

    public void setJwtToken(String jwtToken) { this.jwtToken = jwtToken; }

    public java.util.Date getCreatedAt() { return createdAt; }

    public void setCreatedAt(java.util.Date createdAt) { this.createdAt = createdAt; }
}
