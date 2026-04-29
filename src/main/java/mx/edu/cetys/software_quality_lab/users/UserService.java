package mx.edu.cetys.software_quality_lab.users;

import mx.edu.cetys.software_quality_lab.users.exceptions.UserNotFoundException;
import mx.edu.cetys.software_quality_lab.users.exceptions.DuplicateUsernameException;
import mx.edu.cetys.software_quality_lab.users.exceptions.InvalidUserDataException;
import mx.edu.cetys.software_quality_lab.validators.EmailValidatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final EmailValidatorService emailValidatorService;

    public UserService(UserRepository userRepository, EmailValidatorService emailValidatorService) {
        this.userRepository = userRepository;
        this.emailValidatorService = emailValidatorService;
    }


    private boolean isValidRequest(UserController.UserRequest request)
    {
        // Username validations
        String username = request.username();
        if (username.isEmpty()) {
            return false;
        }

        if (username.length() < 5 || username.length() > 20 ) {
            return false;
        }

        if (!username.matches("[a-z0-9_]+") || username.startsWith("_") || username.endsWith("_")) {
            return false;
        }

        // First name validations
        String firstName = request.firstName();
        if (firstName.isEmpty()) {
            return false;
        }

        if (firstName.length() < 2 || firstName.length() > 50 ) {
            return false;
        }

        if (!firstName.matches("[\\p{L}]+")) {
            return false;
        }

        // Last name validations
        String lastName = request.lastName();
        if (lastName.isEmpty()) {
            return false;
        }

        if (lastName.length() < 2 || lastName.length() > 50 ) {
            return false;
        }

        if (!lastName.matches("[\\p{L}]+")) {
            return false;
        }

        // Age validations
        if (request.age() < 13 || request.age() > 120) {
            return false;
        }

        // Phone validations
        String phone = request.phone();
        if (!phone.matches("\\d{10}")) {
            return false;
        }

        // Email validation
        return emailValidatorService.isValid(request.email());
    }

    private User userRequestMapper(UserController.UserRequest request) {
        User user = new User();
        user.setUsername(request.username());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhone(request.phone());
        user.setEmail(request.email());
        user.setAge(request.age());

        return user;
    }

    private UserController.UserResponse userResponseMapper (User user) {
        return new UserController.UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getEmail(),
                user.getAge(),
                user.getStatus().toString());
    }

    /**
     * Registrar un nuevo usuario aplicando todas las reglas de negocio.
     *
     * Reglas a implementar (lanzar InvalidUserDataException a menos que se indique):
     *  1. Username  — entre 5 y 20 caracteres, solo letras minúsculas, dígitos y guion bajo (_),
     *                 NO debe comenzar ni terminar con guion bajo
     *  2. First name — entre 2 y 50 caracteres, solo letras (se permiten acentos: á, é, ñ, etc.)
     *  3. Last name  — entre 2 y 50 caracteres, solo letras (se permiten acentos)
     *  4. Age        — debe ser mayor a 12 y menor o igual a 120
     *  5. Phone      — exactamente 10 dígitos, sin letras ni símbolos
     *  6. Email      — delegar a emailValidatorService.isValid(email);
     *                  lanzar InvalidUserDataException si regresa false
     *  7. Unicidad del username — si userRepository.existsByUsername regresa true,
     *                             lanzar DuplicateUsernameException
     */
    UserController.UserResponse registerUser(UserController.UserRequest request) {
        log.info("Iniciando registro de usuario, username={}", request.username());

        if (!isValidRequest(request)){
            throw new InvalidUserDataException("Invalid request");
        }

        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateUsernameException("User already exists");
        }

        User user = userRequestMapper(request);

        var savedUser = userRepository.save(user);

        return userResponseMapper(savedUser);
    }

    /**
     * Buscar un usuario por ID.
     * Lanzar UserNotFoundException (HTTP 404) si el usuario no existe.
     */
    UserController.UserResponse getUserById(Long id) {
        log.info("Finding user by ID, id={}", id);

        // buscar por id con findById, lanzar UserNotFoundException si está vacío, mapear y regresar
        var userDb = userRepository.findById(id);

        if (userDb.isEmpty()) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        var realUser = userDb.get();
        return new UserController.UserResponse(
                realUser.getId(),
                realUser.getUsername(),
                realUser.getFirstName(),
                realUser.getLastName(),
                realUser.getPhone(),
                realUser.getEmail(),
                realUser.getAge(),
                realUser.getStatus().toString()
        );


    }

    /**
     * Suspender un usuario ACTIVO.
     * Lanzar UserNotFoundException si el usuario no existe.
     * Lanzar InvalidUserDataException si el usuario ya está SUSPENDED.
     */
    UserController.UserResponse suspendUser(Long id) {
        log.info("Suspendiendo usuario, id={}", id);
        var user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("User not found"));
        if (user.getStatus() == UserStatus.SUSPENDED) throw new InvalidUserDataException("El usuario ya esta suspendido");

        user.setStatus(UserStatus.SUSPENDED);
        User patched = userRepository.save(user);

        return userResponseMapper(patched);
    }

}
