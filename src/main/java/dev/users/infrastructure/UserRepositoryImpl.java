package dev.users.infrastructure;

import dev.users.domain.models.UserEntity;
import dev.users.domain.repository.UserRepository;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepositoryImpl implements UserRepository, PanacheRepository<UserEntity> {

    @Override
    public Uni<UserEntity> persist(UserEntity user) {
        return persistAndFlush(user);
    }

    @Override
    public Uni<UserEntity> findByUsername(String username) {
        return find("username", username).firstResult();
    }

    @Override
    public Uni<UserEntity> findByEmail(String email) {
        return find("email", email).firstResult();
    }
}
