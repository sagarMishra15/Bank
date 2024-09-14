package com.app.Bank.dto;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private final String role;
    private final String jwt;

    public AuthenticationResponse(String role, String jwt) {
        this.role = role;
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
