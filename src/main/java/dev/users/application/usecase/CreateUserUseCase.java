package dev.users.application.usecase;

import dev.users.application.requests.CreateUserRequest;
import dev.users.application.responses.CreateUserResponse;
import dev.users.domain.models.UserEntity;
import dev.users.domain.repository.UserRepository;
import dev.users.utils.PasswordUtils;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CreateUserUseCase {
    private final UserRepository userRepository;

    public CreateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Uni<CreateUserResponse> execute(CreateUserRequest request) {
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(PasswordUtils.encrypt(request.getPassword()));

        return userRepository.persist(user)
                .map(v -> {
                    CreateUserResponse response = new CreateUserResponse();
                    response.setUsername(user.getUsername());
                    response.setEmail(user.getEmail());
                    response.setHash(user.getHash());
                    return response;
                });
    }
}
