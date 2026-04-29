package mx.edu.cetys.software_quality_lab.petstore;

import mx.edu.cetys.software_quality_lab.commons.ApiResponse;
import mx.edu.cetys.software_quality_lab.petstore.exceptions.AdoptionNotFoundException;
import mx.edu.cetys.software_quality_lab.petstore.exceptions.MaxAdoptionsReachedException;
import mx.edu.cetys.software_quality_lab.petstore.exceptions.PetAlreadyAdoptedException;
import mx.edu.cetys.software_quality_lab.petstore.exceptions.UserNotEligibleException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// @RestControllerAdvice intercepta excepciones lanzadas por cualquier @RestController
// y las convierte en respuestas HTTP con el status code apropiado
// PetNotFoundException ya es manejada globalmente por PetControllerAdvice del módulo pets/
@RestControllerAdvice
public class PetstoreControllerAdvice {

    // TODO: regresar HTTP 404 cuando la adopción no se encuentra en BD
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiResponse<Void> handleAdoptionNotFound(AdoptionNotFoundException ex) {
        throw new UnsupportedOperationException("TODO: implementar handleAdoptionNotFound");
    }

    // TODO: regresar HTTP 409 cuando el pet ya tiene una adopción activa
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    ApiResponse<Void> handlePetAlreadyAdopted(PetAlreadyAdoptedException ex) {
        throw new UnsupportedOperationException("TODO: implementar handlePetAlreadyAdopted");
    }

    // TODO: regresar HTTP 422 cuando el usuario no es elegible (suspendido o menor de 18)
    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    ApiResponse<Void> handleUserNotEligible(UserNotEligibleException ex) {
        throw new UnsupportedOperationException("TODO: implementar handleUserNotEligible");
    }

    // TODO: regresar HTTP 422 cuando el usuario ya tiene 3 adopciones activas
    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    ApiResponse<Void> handleMaxAdoptionsReached(MaxAdoptionsReachedException ex) {
        throw new UnsupportedOperationException("TODO: implementar handleMaxAdoptionsReached");
    }
}
