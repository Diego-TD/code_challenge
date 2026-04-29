package mx.edu.cetys.software_quality_lab.users;

import mx.edu.cetys.software_quality_lab.users.exceptions.DuplicateUsernameException;
import mx.edu.cetys.software_quality_lab.users.exceptions.InvalidUserDataException;
import mx.edu.cetys.software_quality_lab.users.exceptions.UserNotFoundException;
import mx.edu.cetys.software_quality_lab.validators.EmailValidatorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
        // TODO: arrange — construir un UserRequest válido, mockear emailValidatorService.isValid para que regrese true,
        //       mockear userRepository.existsByUsername para que regrese false,
        //       mockear userRepository.save para que regrese un User con id
        // TODO: act — llamar a userService.registerUser(request)
        // TODO: assert — verificar id, username, status == "ACTIVE"; confirmar que save fue llamado una vez
    }

    @Test
    void shouldGetUserByIdSuccessfully() {
        // TODO: arrange — mockear userRepository.findById para que regrese un Optional<User> con datos
        // TODO: act — llamar a userService.getUserById(1L)
        // TODO: assert — verificar que los campos del response coincidan con el mock
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
        // TODO: construir request con username de 4 caracteres
        // TODO: assertThrows InvalidUserDataException
    }

    @Test
    void shouldThrowWhenUsernameTooLong() {
        // TODO: construir request con username de 21 caracteres
        // TODO: assertThrows InvalidUserDataException
    }

    @Test
    void shouldThrowWhenUsernameHasInvalidChars() {
        // TODO: username con mayúsculas o caracteres especiales, ej. "User@Name"
        // TODO: assertThrows InvalidUserDataException
    }

    @Test
    void shouldThrowWhenUsernameStartsWithUnderscore() {
        // TODO: username "_nombrevalido"
        // TODO: assertThrows InvalidUserDataException
    }

    @Test
    void shouldThrowWhenUsernameEndsWithUnderscore() {
        // TODO: username "nombrevalido_"
        // TODO: assertThrows InvalidUserDataException
    }

    // ─── Validaciones de Nombre ───────────────────────────────────────────────

    @Test
    void shouldThrowWhenFirstNameTooShort() {
        // TODO: firstName de 1 carácter
        // TODO: assertThrows InvalidUserDataException
    }

    @Test
    void shouldThrowWhenFirstNameContainsNumbers() {
        // TODO: firstName como "Juan5"
        // TODO: assertThrows InvalidUserDataException
    }

    @Test
    void shouldThrowWhenLastNameTooShort() {
        // TODO: lastName de 1 carácter
        // TODO: assertThrows InvalidUserDataException
    }

    @Test
    void shouldThrowWhenLastNameContainsNumbers() {
        // TODO: lastName como "Perez2"
        // TODO: assertThrows InvalidUserDataException
    }

    // ─── Validaciones de Age ─────────────────────────────────────────────────

    @Test
    void shouldThrowWhenAgeIsExactlyTwelve() {
        // TODO: age = 12 — caso límite (boundary): debe ser MAYOR a 12, no mayor o igual
        // TODO: assertThrows InvalidUserDataException
    }

    @Test
    void shouldThrowWhenAgeIsBelowTwelve() {
        // TODO: age = 5
        // TODO: assertThrows InvalidUserDataException
    }

    @Test
    void shouldThrowWhenAgeExceedsMaximum() {
        // TODO: age = 121 — excede el máximo permitido de 120
        // TODO: assertThrows InvalidUserDataException
    }

    // ─── Validaciones de Phone ───────────────────────────────────────────────

    @Test
    void shouldThrowWhenPhoneHasWrongLength() {
        // TODO: phone con 9 u 11 dígitos
        // TODO: assertThrows InvalidUserDataException
    }

    @Test
    void shouldThrowWhenPhoneContainsLetters() {
        // TODO: phone como "123456789a"
        // TODO: assertThrows InvalidUserDataException
    }

    // ─── Validación de Email ──────────────────────────────────────────────────

    @Test
    void shouldThrowWhenEmailIsInvalid() {
        // TODO: mockear emailValidatorService.isValid(anyString()) para que regrese false
        // TODO: assertThrows InvalidUserDataException
        // TODO: verificar que emailValidatorService.isValid fue llamado (verify)
    }

    // ─── Unicidad de Username ─────────────────────────────────────────────────

    @Test
    void shouldThrowWhenUsernameAlreadyExists() {
        // TODO: mockear emailValidatorService.isValid para que regrese true
        // TODO: mockear userRepository.existsByUsername para que regrese true
        // TODO: assertThrows DuplicateUsernameException
        // TODO: verificar que userRepository.save NUNCA fue llamado (verify never)
    }

    // ─── Not found ───────────────────────────────────────────────────────────

    @Test
    void shouldThrowWhenUserNotFound() {
        // TODO: mockear userRepository.findById para que regrese Optional.empty()
        // TODO: assertThrows UserNotFoundException
    }

    @Test
    void shouldThrowWhenSuspendingAlreadySuspendedUser() {
        // TODO: mockear findById con un usuario SUSPENDED
        // TODO: assertThrows InvalidUserDataException
    }
}
