package dev.users.application.usecase;

import java.util.HashSet;
import java.util.Optional;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;

import dev.users.application.requests.AuthenticateRequest;
import dev.users.application.responses.AuthenticateResponse;
import dev.users.domain.models.UserEntity;
import dev.users.domain.repository.UserRepository;
import dev.users.utils.PasswordUtils;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AuthenticateUseCase {

    /** Configure the issuer for JWT generation. */
    @ConfigProperty(name = "users.issuer")
    Optional<String> issuer;

    private final UserRepository userRepository;

    public AuthenticateUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Uni<AuthenticateResponse> execute(AuthenticateRequest request) {
        return userRepository.findByEmail(request.getEmail())
        .onItem().ifNull()
        .failWith(new IllegalArgumentException("Wrong email or password"))
        .onItem().ifNotNull()
        .transformToUni(user -> {
            authenticate(request.getPassword(), user.getPassword());
            AuthenticateResponse response = new AuthenticateResponse();
            response.setJwt(generateJWT(user));
            return Uni.createFrom().item(response);
        });

    }

    private Uni<AuthenticateResponse> authenticate(final String password, final String encryptedpassword) {
        if (PasswordUtils.checkPassword(password, encryptedpassword) == false) {
            throw new IllegalArgumentException("Wrong email or password");
        }
        return null;
    }

    /**
     * Creates a JWT (JSON Web Token) to a user.
     *
     * @param user : The user object
     *
     * @return Returns the JWT
     */
    private String generateJWT(final UserEntity user) {
        HashSet<String> groups = new HashSet<>();
        groups.add("user");
        return Jwt.issuer("users-pw2")
            .upn(user.getEmail())
            .groups(groups)
            .claim(Claims.c_hash, user.getHash())
            .claim(Claims.email, user.getEmail())
            .sign();
    }

}

