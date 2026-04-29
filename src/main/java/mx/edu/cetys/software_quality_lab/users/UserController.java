package mx.edu.cetys.software_quality_lab.users;

import mx.edu.cetys.software_quality_lab.commons.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    // DTOs (Data Transfer Objects) — definen la forma del request y del response
    record UserRequest(String username, String firstName, String lastName, String phone, String email, Integer age) {}
    record UserResponse(Long id, String username, String firstName, String lastName, String phone, String email, Integer age, String status) {}
    record UserWrapper(UserResponse user) {}

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // POST /users — registrar un nuevo usuario
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // HTTP 201: recurso creado exitosamente
    ApiResponse<UserWrapper> registerUser(@RequestBody UserRequest request) {
        // TODO: llamar a userService.registerUser, envolver en ApiResponse y regresar
        throw new UnsupportedOperationException("TODO: implementar endpoint registerUser");
    }

    // GET /users/{id} — obtener un usuario por ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK) // HTTP 200: solicitud exitosa
    ApiResponse<UserWrapper> getUserById(@PathVariable Long id) {
        // TODO: llamar a userService.getUserById, envolver en ApiResponse y regresar
        throw new UnsupportedOperationException("TODO: implementar endpoint getUserById");
    }

    // PATCH /users/{id}/suspend — suspender un usuario activo
    @PatchMapping("/{id}/suspend")
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<UserWrapper> suspendUser(@PathVariable Long id) {
        // TODO: llamar a userService.suspendUser, envolver en ApiResponse y regresar
        throw new UnsupportedOperationException("TODO: implementar endpoint suspendUser");
    }
}
