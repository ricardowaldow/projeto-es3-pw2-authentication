package dev.users.domain.dto.responses;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class AuthenticateResponse {
    public String jwt;
}
