package mx.edu.cetys.software_quality_lab.pets;

import mx.edu.cetys.software_quality_lab.commons.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// @RestController indica que esta clase maneja peticiones HTTP y regresa JSON automáticamente
// @RequestMapping define el prefijo de ruta para todos los endpoints de este controller
@RestController
@RequestMapping("/pets")
public class PetController {

    // HTTP Verbs disponibles y sus rutas planeadas:
    // POST   /pets          → crear un nuevo Pet
    // GET    /pets          → obtener todos los Pets
    // GET    /pets/{id}     → obtener un Pet por ID
    // PUT    /pets/{id}     → reemplazar un Pet completo
    // PATCH  /pets/{id}     → actualizar campos específicos de un Pet
    // DELETE /pets/{id}     → eliminar un Pet

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    // DTOs (Data Transfer Objects): definen la forma del JSON de entrada (Request) y salida (Response)
    // Se usan records de Java para mantenerlos inmutables y concisos — equivalente a una clase con solo campos y getters
    record PetRequest(String name, String color, String race, Integer age) {}
    record PetResponse(Long id, String name, String color, String race, Integer age, boolean available) {}

    // Wrapper: envuelve el PetResponse dentro del campo "pet" del JSON de respuesta
    record PetWrapper(PetResponse pet) {}

    // Endpoint de ayuda
    @GetMapping("/help")
    ApiResponse<PetWrapper> help() {
        return new ApiResponse<>("API de Pets — usa POST /pets para crear y GET /pets/{id} para buscar", null, null);
    }

    // POST /pets — crear un nuevo Pet
    // @ResponseStatus(HttpStatus.CREATED) indica que si todo sale bien, el HTTP status es 201
    // 201 significa "recurso creado exitosamente"
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<PetWrapper> createPet(@RequestBody PetRequest requestPet) {
        // @RequestBody deserializa el JSON del body del request a un objeto PetRequest
        var savedPet = petService.savePet(requestPet);
        return new ApiResponse<>("Pet creado exitosamente", new PetWrapper(savedPet), null);
    }

    // GET /pets/{id} — buscar un Pet por ID
    // @PathVariable extrae el valor del segmento {petId} de la URL
    @GetMapping("/{petId}")
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<PetWrapper> findPetById(@PathVariable Long petId) {
        var pet = petService.getPetById(petId);
        return new ApiResponse<>("Pet encontrado", new PetWrapper(pet), null);
    }

    // GET /pets — obtener todos los Pets
    // Si no hay pets en BD, regresa lista vacía — nunca un error
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<List<PetResponse>> getAllPets() {
        var pets = petService.getAllPets();
        return new ApiResponse<>("Pets encontrados: " + pets.size(), pets, null);
    }

    // PATCH /pets/{id}/available — marcar un pet como disponible para adopción en el petstore
    @PatchMapping("/{petId}/available")
    @ResponseStatus(HttpStatus.OK)
    ApiResponse<PetWrapper> markPetAvailable(@PathVariable Long petId) {
        var pet = petService.markAvailable(petId);
        return new ApiResponse<>("Pet marcado como disponible para adopción", new PetWrapper(pet), null);
    }
}
