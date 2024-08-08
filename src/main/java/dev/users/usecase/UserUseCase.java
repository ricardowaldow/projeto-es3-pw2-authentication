package dev.users.usecase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dev.users.models.UserEntity;
import dev.users.repository.UserRepository;
import dev.users.utils.JWTGenerator;
import dev.users.utils.PasswordUtils;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserUseCase {

    private final UserRepository userRepository;

    public UserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Uni<UserEntity> createUser(final String username, final String email, final String password) {
        if ((username == null || username.isBlank())
        || (email == null || email.isBlank())
        || (password == null || password.isEmpty())) {
            throw new IllegalArgumentException("Todos os campos devem ser preenchidos");
        } else if(!isValidEmail(email)) {
            throw new IllegalArgumentException("Email Invalido");
        } else if(password.length() < 8) {
            throw new IllegalArgumentException("A senha deve ter no mínimo 8 caracteres");
        }
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(PasswordUtils.encrypt(password));

        return userRepository.checkAndPersist(user);
    }

    public Uni<String> authenticateUser(final String email, final String password) {
        if ((email == null || email.isBlank())
        || (password == null || password.isEmpty())) {
            throw new IllegalArgumentException("Todos os campos devem ser preenchidos");
        } else if(!isValidEmail(email)) {
            throw new IllegalArgumentException("Email Invalido");
        }
        return userRepository.findByEmail(email)
        .onItem().ifNull()
        .failWith(new IllegalArgumentException("Wrong email or password"))
        .onItem().ifNotNull()
        .transformToUni(user -> {
            authenticate(password, user.getPassword());
            String response = JWTGenerator.generateJWT(user);
            return Uni.createFrom().item(response);
        });

    }

    private Uni<String> authenticate(final String password, final String encryptedpassword) {
        if (!PasswordUtils.checkPassword(password, encryptedpassword)) {
            throw new IllegalArgumentException("Wrong email or password");
        }
        return null;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
