package com.securevote.secure_voting.dto;

public class VoteRequest {
    private String encryptedVote;
    private String hash;

    // Getters and Setters
    public String getEncryptedVote() {
        return encryptedVote;
    }

    public void setEncryptedVote(String encryptedVote) {
        this.encryptedVote = encryptedVote;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
