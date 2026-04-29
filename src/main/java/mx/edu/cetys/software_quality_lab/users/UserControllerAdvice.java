package mx.edu.cetys.software_quality_lab.users;

import mx.edu.cetys.software_quality_lab.commons.ApiResponse;
import mx.edu.cetys.software_quality_lab.users.exceptions.DuplicateUsernameException;
import mx.edu.cetys.software_quality_lab.users.exceptions.InvalidUserDataException;
import mx.edu.cetys.software_quality_lab.users.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// @RestControllerAdvice intercepta excepciones lanzadas por cualquier @RestController
// y las convierte en respuestas HTTP con el status code apropiado
@RestControllerAdvice
public class UserControllerAdvice {

    // TODO: regresar HTTP 400 cuando los datos del usuario son inválidos
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ApiResponse<Void> handleInvalidUserData(InvalidUserDataException ex) {
        throw new UnsupportedOperationException("TODO: implementar handleInvalidUserData");
    }

    // TODO: regresar HTTP 404 cuando el usuario no se encuentra en BD
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiResponse<Void> handleUserNotFound(UserNotFoundException ex) {
        throw new UnsupportedOperationException("TODO: implementar handleUserNotFound");
    }

    // TODO: regresar HTTP 409 cuando el username ya está registrado
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    ApiResponse<Void> handleDuplicateUsername(DuplicateUsernameException ex) {
        throw new UnsupportedOperationException("TODO: implementar handleDuplicateUsername");
    }
}
