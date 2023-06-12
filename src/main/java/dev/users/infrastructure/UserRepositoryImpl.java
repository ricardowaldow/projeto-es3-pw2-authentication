package dev.users.infrastructure;

import dev.users.data.repository.UserRepository;
import dev.users.domain.models.UserEntity;
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

    @Override
    public Uni<UserEntity> checkAndPersist(UserEntity user) {
        String email = user.getEmail();
        return find("email", email).firstResult()
        .onItem().ifNotNull().failWith(new IllegalArgumentException("Email já está em uso"))
        .onItem().ifNull().switchTo(this.persist(user));
    }
}
