package dev.users.utils;

import java.time.Duration;
import java.util.HashSet;

import org.eclipse.microprofile.jwt.Claims;

import dev.users.domain.models.UserEntity;
import io.smallrye.jwt.build.Jwt;

public class JWTGenerator {

    /**
     * Creates a JWT (JSON Web Token) to a user.
     *
     * @param user : The user object
     *
     * @return Returns the JWT
     */
    public static String generateJWT(final UserEntity user) {
        HashSet<String> groups = new HashSet<>();
        groups.add("user");
        return Jwt.issuer("users-pw2")
                .upn(user.getEmail())
                .expiresIn(Duration.ofDays(365))
                .groups(groups)
                .claim(Claims.c_hash, user.getHash())
                .sign();
    }

}
