package dev.users.application.responses;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class CreateUserResponse {

    private String hash;
    private String username;
    private String email;

}
