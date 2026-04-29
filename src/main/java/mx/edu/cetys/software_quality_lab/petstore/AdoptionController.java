package mx.edu.cetys.software_quality_lab.petstore;

import mx.edu.cetys.software_quality_lab.commons.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/petstore")
public class AdoptionController {

    // DTOs — definen la forma del request y del response para el módulo de adopciones
    record AdoptionRequest(Long userId, Long petId) {}
    record AdoptionResponse(Long id, Long userId, Long petId, String status, String adoptionDate) {}
    record AdoptionWrapper(AdoptionResponse adoption) {}

    // DTO para listar pets disponibles — vista simplificada del Pet para el petstore
    record AvailablePetResponse(Long id, String name, String race, String color, Integer age) {}

    private final AdoptionService adoptionService;

    public AdoptionController(AdoptionService adoptionService) {
        this.adoptionService = adoptionService;
    }

    // GET /petstore/pets — listar todos los pets disponibles para adopción
    @GetMapping("/pets")
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<List<AvailablePetResponse>> listAvailablePets() {
        // TODO: llamar a adoptionService.listAvailablePets, envolver en ApiResponse y regresar
        throw new UnsupportedOperationException("TODO: implementar endpoint listAvailablePets");
    }

    // POST /petstore/adoptions — crear una nueva adopción
    @PostMapping("/adoptions")
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<AdoptionWrapper> createAdoption(@RequestBody AdoptionRequest request) {
        // TODO: llamar a adoptionService.createAdoption, envolver en ApiResponse y regresar
        throw new UnsupportedOperationException("TODO: implementar endpoint createAdoption");
    }

    // PATCH /petstore/adoptions/{id}/cancel — cancelar una adopción activa
    @PatchMapping("/adoptions/{id}/cancel")
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<AdoptionWrapper> cancelAdoption(@PathVariable Long id) {
        // TODO: llamar a adoptionService.cancelAdoption, envolver en ApiResponse y regresar
        throw new UnsupportedOperationException("TODO: implementar endpoint cancelAdoption");
    }
}
