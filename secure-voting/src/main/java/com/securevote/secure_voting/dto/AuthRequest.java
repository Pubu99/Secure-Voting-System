package com.securevote.secure_voting.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}