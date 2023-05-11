package dev.users.application.requests;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class AuthenticateRequest {
    private String email;
    private String password;
}
