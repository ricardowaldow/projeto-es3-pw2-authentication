package dev.users.data.repository;

import dev.users.domain.models.UserEntity;
import io.smallrye.mutiny.Uni;

public interface UserRepository {
    Uni<UserEntity> persist(UserEntity user);
    Uni<UserEntity> findByUsername(String username);
    Uni<UserEntity> findByEmail(String email);
    Uni<UserEntity> checkAndPersist(UserEntity user);
}
