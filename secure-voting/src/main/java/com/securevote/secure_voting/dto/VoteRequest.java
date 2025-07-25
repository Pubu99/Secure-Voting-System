package com.securevote.secure_voting.dto;

import lombok.Data;

@Data
public class VoteRequest {
    private String encryptedVote;
    private String hash;
}