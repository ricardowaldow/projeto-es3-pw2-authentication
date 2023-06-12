package dev.users.application.usecase;

import dev.users.data.repository.UserRepository;
import dev.users.domain.dto.requests.CreateUserRequest;
import dev.users.domain.dto.responses.CreateUserResponse;
import dev.users.domain.models.UserEntity;
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
        if ((request.getUsername() == null || request.getUsername().isBlank())
        || (request.getEmail() == null || request.getEmail().isBlank())
        || (request.getPassword() == null || request.getPassword().isEmpty())) {
            throw new IllegalArgumentException("Todos os campos devem ser preenchidos");
        }
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(PasswordUtils.encrypt(request.getPassword()));

        return userRepository.checkAndPersist(user)
                .map(v -> {
                    CreateUserResponse response = new CreateUserResponse();
                    response.setUsername(user.getUsername());
                    response.setEmail(user.getEmail());
                    response.setHash(user.getHash());
                    return response;
                });
    }
}
