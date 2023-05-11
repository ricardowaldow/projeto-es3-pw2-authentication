package dev.users.application.responses;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class AuthenticateResponse {
    public String jwt;
}
