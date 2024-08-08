package dev.users;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.users.models.UserEntity;
import dev.users.repository.UserRepository;
import dev.users.usecase.UserUseCase;
import dev.users.utils.PasswordUtils;
import io.smallrye.mutiny.Uni;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class UnitTest {

    @Mock
    UserRepository repository;

    @InjectMocks
    UserUseCase uc;

    @Test
    @DisplayName("Create a User")
    @Order(1)
    public void createUser() {
        UserEntity entity = new UserEntity();
        entity.setUsername("username");
        entity.setEmail("email@email.com");
        entity.setPassword(PasswordUtils.encrypt("password123"));
        Mockito
                .when(repository.checkAndPersist(Mockito.any(UserEntity.class)))
                .thenReturn(Uni.createFrom().item(entity));
        Uni<UserEntity> uni = uc.createUser("username", "email@email.com", "password123");
        assertNotNull(uni);
    }

    @Test
    @DisplayName("Create a User with blank username")
    @Order(2)
    public void createUserWithBlankUsername() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            uc.createUser("", "email@email.com", "username");
        });
    }

    @Test
    @DisplayName("Create a User with blank email")
    @Order(3)
    public void createUserWithBlankEmail() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            uc.createUser("username", "", "password123");
        });
    }

    @Test
    @DisplayName("Create a User with blank password")
    @Order(4)
    public void createUserWithBlankPassword() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            uc.createUser("username", "email@email.com", "");
        });
    }

    @Test
    @DisplayName("Create a User with invalid email")
    @Order(5)
    public void createUserWithInvalidEmail() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            uc.createUser("username", "invalidEmail", "password123");
        });
    }

    @Test
    @DisplayName("Create a User with short password")
    @Order(6)
    public void createUserWithShortPassword() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            uc.createUser("username", "email@email.com", "pass");
        });
    }

    @Test
    @DisplayName("Authenticate a User")
    @Order(7)
    public void authenticateUser() {
        UserEntity entity = new UserEntity();
        entity.setUsername("username");
        entity.setEmail("email@email.com");
        entity.setPassword(PasswordUtils.encrypt("password123"));
        Mockito
                .when(repository.findByEmail(entity.getEmail()))
                .thenReturn(Uni.createFrom().item(entity));
        Uni<String> uni = uc.authenticateUser("email@email.com", "password123");
        assertNotNull(uni);
    }

    @Test
    @DisplayName("Authenticate a User with blank email")
    @Order(8)
    public void authenticateUserWithBlankUsername() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            uc.authenticateUser("", "password123");
        });
    }

    @Test
    @DisplayName("Authenticate a User with empty password")
    @Order(9)
    public void authenticateUserWithEmptyPassword() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            uc.authenticateUser("email@email.com", "");
        });
    }

    @Test
    @DisplayName("Authenticate a User with invalid email")
    @Order(10)
    public void authenticateUserWithInvalidEmail() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            uc.authenticateUser("emailemail", "password123");
        });
    }

}
