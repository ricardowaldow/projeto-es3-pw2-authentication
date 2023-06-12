package dev.users.domain.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter@Setter
public class UserEntity extends PanacheEntityBase {

    /** Primary key. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    /** User hash */
    private String hash;

    /** Username. */
    private String username;

    /** Email. */
    @Email(message = "Invalid Email")
    private String email;

    /** Password. */
    private String password;

    public UserEntity() {
        super();
        this.hash = UUID.randomUUID().toString();
    }

}
