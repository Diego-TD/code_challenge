package mx.edu.cetys.software_quality_lab.users;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Optional;
import mx.edu.cetys.software_quality_lab.users.exceptions.DuplicateUsernameException;
import mx.edu.cetys.software_quality_lab.users.exceptions.InvalidUserDataException;
import mx.edu.cetys.software_quality_lab.users.exceptions.UserNotFoundException;
import mx.edu.cetys.software_quality_lab.validators.EmailValidatorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    // EmailValidatorService debe ser mockeado — en pruebas unitarias no probamos dependencias externas
    @Mock
    EmailValidatorService emailValidatorService;

    @InjectMocks
    UserService userService;

    // ─── Caso exitoso ─────────────────────────────────────────────────────────

    @Test
    void shouldRegisterUserSuccessfully() {
        // Arrange
        UserController.UserRequest request = new UserController.UserRequest(
            "juan4_dev",
            "Juan",
            "Pérez",
            "6641234567",
            "hola#gm4l.com",
            25
        );

        when(emailValidatorService.isValid(anyString())).thenReturn(true);
        when(userRepository.existsByUsername(request.username())).thenReturn(
            false
        );

        User saved = new User(
            "juan4_dev",
            "Juan",
            "Pérez",
            "6641234567",
            "hola#gm4l.com",
            25
        );

        saved.setId(1L);
        when(userRepository.save(any(User.class))).thenReturn(saved);

        // Act
        UserController.UserResponse response = userService.registerUser(
            request
        );

        // Assert
        Mockito.verify(userRepository, times(1)).save(any(User.class));
        assertEquals(1, response.id());
        assertEquals("juan4_dev", response.username());
        assertEquals("ACTIVE", response.status());
    }

    @Test
    void shouldGetUserByIdSuccessfully() {
        // arrange — mockear userRepository.findById para que regrese un Optional<User> con datos
        Long userId = 1L;
        User mockUser = new User("user1", "Usu", "Lopez", "12345", "email", 8);
        mockUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // act — llamar a userService.getUserById(1L)
        UserController.UserResponse userResponse = userService.getUserById(
            userId
        );

        // assert — verificar que los campos del response coincidan con el mock
        assertEquals(mockUser.getUsername(), userResponse.username());
        assertEquals(mockUser.getFirstName(), userResponse.firstName());
        assertEquals(mockUser.getLastName(), userResponse.lastName());
        assertEquals(mockUser.getEmail(), userResponse.email());
        assertEquals(mockUser.getPhone(), userResponse.phone());
        assertEquals(mockUser.getAge(), userResponse.age());
    }

    @Test
    void shouldSuspendActiveUserSuccessfully() {
        // TODO: arrange — mockear findById con un usuario ACTIVE
        // TODO: act — llamar a userService.suspendUser(id)
        // TODO: assert — verificar que el status regresado sea "SUSPENDED"; confirmar que save fue llamado
    }

    // ─── Validaciones de Username ─────────────────────────────────────────────

    @Test
    void shouldThrowWhenUsernameTooShort() {
        UserController.UserRequest request = new UserController.UserRequest(
            "juan",
            "Juan",
            "Pérez",
            "6641234567",
            "hola#gm4l.com",
            25
        );

        assertThrows(InvalidUserDataException.class, () ->
            userService.registerUser(request)
        );
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowWhenUsernameTooLong() {
        UserController.UserRequest request =
                new UserController.UserRequest(
                        "juanpacopedrodelamarr",
                        "Juan",
                        "Pérez",
                        "6641234567",
                        "hola#gm4l.com",
                        25);

        assertThrows(InvalidUserDataException.class, () -> userService.registerUser(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowWhenUsernameHasInvalidChars() {
        UserController.UserRequest request =
                new UserController.UserRequest(
                        "User@Name",
                        "Juan",
                        "Pérez",
                        "6641234567",
                        "hola#gm4l.com",
                        25);

        assertThrows(InvalidUserDataException.class, () -> userService.registerUser(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowWhenUsernameStartsWithUnderscore() {
        UserController.UserRequest request =
                new UserController.UserRequest(
                        "_nombrevalido",
                        "Juan",
                        "Pérez",
                        "6641234567",
                        "hola#gm4l.com",
                        25);

        assertThrows(InvalidUserDataException.class, () -> userService.registerUser(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowWhenUsernameEndsWithUnderscore() {
        UserController.UserRequest request =
                new UserController.UserRequest(
                        "nombrevalido_",
                        "Juan",
                        "Pérez",
                        "6641234567",
                        "hola#gm4l.com",
                        25);

        assertThrows(InvalidUserDataException.class, () -> userService.registerUser(request));
        verify(userRepository, never()).save(any(User.class));
    }

    // ─── Validaciones de Nombre ───────────────────────────────────────────────

    @Test
    void shouldThrowWhenFirstNameTooShort() {
        UserController.UserRequest request =
                new UserController.UserRequest(
                        "juan4_dev",
                        "J",
                        "Pérez",
                        "6641234567",
                        "hola#gm4l.com",
                        25);

        assertThrows(InvalidUserDataException.class, () -> userService.registerUser(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowWhenFirstNameContainsNumbers() {
        UserController.UserRequest request =
                new UserController.UserRequest(
                        "juan4_dev",
                        "Juan5",
                        "Pérez",
                        "6641234567",
                        "hola#gm4l.com",
                        25);

        assertThrows(InvalidUserDataException.class, () -> userService.registerUser(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowWhenLastNameTooShort() {
        UserController.UserRequest request =
                new UserController.UserRequest(
                        "juan4_dev",
                        "Juan",
                        "P",
                        "6641234567",
                        "hola#gm4l.com",
                        25);

        assertThrows(InvalidUserDataException.class, () -> userService.registerUser(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowWhenLastNameContainsNumbers() {
        UserController.UserRequest request =
                new UserController.UserRequest(
                        "juan4_dev",
                        "Juan",
                        "Perez2",
                        "6641234567",
                        "hola#gm4l.com",
                        25);

        assertThrows(InvalidUserDataException.class, () -> userService.registerUser(request));
        verify(userRepository, never()).save(any(User.class));
    }

    // ─── Validaciones de Age ─────────────────────────────────────────────────

    @Test
    void shouldThrowWhenAgeIsExactlyTwelve() {
        UserController.UserRequest request =
                new UserController.UserRequest(
                        "juan4_dev",
                        "Juan",
                        "Pérez",
                        "6641234567",
                        "hola#gm4l.com",
                        12);

        assertThrows(InvalidUserDataException.class, () -> userService.registerUser(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowWhenAgeIsBelowTwelve() {
        UserController.UserRequest request =
                new UserController.UserRequest(
                        "juan4_dev",
                        "Juan",
                        "Pérez",
                        "6641234567",
                        "hola#gm4l.com",
                        5);

        assertThrows(InvalidUserDataException.class, () -> userService.registerUser(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowWhenAgeExceedsMaximum() {
        UserController.UserRequest request =
                new UserController.UserRequest(
                        "juan4_dev",
                        "Juan",
                        "Pérez",
                        "6641234567",
                        "hola#gm4l.com",
                        122);

        assertThrows(InvalidUserDataException.class, () -> userService.registerUser(request));
        verify(userRepository, never()).save(any(User.class));
    }

    // ─── Validaciones de Phone ───────────────────────────────────────────────

    @Test
    void shouldThrowWhenPhoneHasWrongLength() {
        UserController.UserRequest request =
                new UserController.UserRequest(
                        "juan4_dev",
                        "Juan",
                        "Pérez",
                        "664123456",
                        "hola#gm4l.com",
                        25);

        assertThrows(InvalidUserDataException.class, () -> userService.registerUser(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowWhenPhoneContainsLetters() {
        UserController.UserRequest request =
                new UserController.UserRequest(
                        "juan4_dev",
                        "Juan",
                        "Pérez",
                        "664123456a",
                        "hola#gm4l.com",
                        25);

        assertThrows(InvalidUserDataException.class, () -> userService.registerUser(request));
        verify(userRepository, never()).save(any(User.class));
    }

    // ─── Validación de Email ──────────────────────────────────────────────────

    @Test
    void shouldThrowWhenEmailIsInvalid() {
        UserController.UserRequest request =
                new UserController.UserRequest(
                        "juan4_dev",
                        "Juan",
                        "Pérez",
                        "6641234567",
                        "hola@gm4l.com",
                        25);

        when(emailValidatorService.isValid(anyString())).thenReturn(false);

        assertThrows(InvalidUserDataException.class, () -> userService.registerUser(request));
        verify(emailValidatorService, times(1)).isValid(request.email());
        verify(userRepository, never()).save(any(User.class));

    }

    // ─── Unicidad de Username ─────────────────────────────────────────────────

    @Test
    void shouldThrowWhenUsernameAlreadyExists() {
        UserController.UserRequest request = new UserController.UserRequest(
                "juan4_dev",
                "Juan",
                "Pérez",
                "6641234567",
                "hola#gm4l.com",
                25
        );


        when(emailValidatorService.isValid(anyString())).thenReturn(true);
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        assertThrows(DuplicateUsernameException.class, () -> userService.registerUser(request));

        verify(emailValidatorService, times(1)).isValid(request.email());
        verify(userRepository, times(1)).existsByUsername(request.username());
        verify(userRepository, never()).save(any(User.class));
    }

    // ─── Not found ───────────────────────────────────────────────────────────

    @Test
    void shouldThrowWhenUserNotFound() {
        // mockear userRepository.findById para que regrese Optional.empty()
        Long userId = 1L;
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        // assertThrows UserNotFoundException
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(userId);
        });
    }

    @Test
    void shouldThrowWhenSuspendingAlreadySuspendedUser() {
        //Arrange
        var user = new User("junperez","Juan", "Perez", "1234567891","jusn#gmil.com",25);
        user.setStatus(UserStatus.SUSPENDED);
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        assertThrows(InvalidUserDataException.class, () -> userService.suspendUser(10L));
        verify(userRepository,never()).save(any());

    }
}
