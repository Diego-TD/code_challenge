package mx.edu.cetys.software_quality_lab.pets;

import mx.edu.cetys.software_quality_lab.pets.exceptions.InvalidPetDataException;
import mx.edu.cetys.software_quality_lab.pets.exceptions.PetNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetService {
    private final Logger log = LoggerFactory.getLogger(PetService.class);

    // Spring inyecta el repository automáticamente — @Autowired no es necesario desde Spring 4.3
    // Repository: clase que se encarga de hacer las operaciones en la base de datos (BD)
    private final PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    // Crear un nuevo Pet
    PetController.PetResponse savePet(PetController.PetRequest requestPet) {
        log.info("Iniciando validaciones del Pet, requestPet={}", requestPet);

        // Regla 1 — Nombre: no puede ser nulo, vacío o menor a 2 caracteres
        if (requestPet.name() == null || requestPet.name().isBlank() || requestPet.name().length() < 2) {
            throw new InvalidPetDataException("El nombre del pet es inválido: mínimo 2 caracteres");
        }

        // Regla 2 — Raza: no puede ser nula ni vacía
        if (requestPet.race() == null || requestPet.race().isBlank()) {
            throw new InvalidPetDataException("La raza del pet no puede estar vacía");
        }

        // Regla 3 — Color: no puede ser nulo ni vacío
        if (requestPet.color() == null || requestPet.color().isBlank()) {
            throw new InvalidPetDataException("El color del pet no puede estar vacío");
        }

        // Regla 4 — Edad: no puede ser nula ni negativa (0 es válido para cachorros recién nacidos)
        if (requestPet.age() == null || requestPet.age() < 0) {
            throw new InvalidPetDataException("La edad del pet es inválida: debe ser 0 o mayor");
        }

        // Todas las validaciones pasaron — guardar en la BD
        // El repository regresa el mismo objeto pero con el ID generado por la BD
        var savedPet = petRepository.save(
                new Pet(requestPet.name(), requestPet.race(), requestPet.color(), requestPet.age())
        );

        log.info("Pet guardado exitosamente, id={}", savedPet.getId());
        return mapToResponse(savedPet);
    }

    // Obtener un Pet por ID
    public PetController.PetResponse getPetById(Long petId) {
        log.info("Buscando Pet por ID, petId={}", petId);

        // Validar que el ID sea un número positivo antes de consultar la BD
        if (petId == null || petId <= 0) {
            throw new InvalidPetDataException("El ID del pet debe ser un número positivo");
        }

        // findById regresa un Optional<Pet> — Optional es un contenedor que puede tener valor o estar vacío
        // Se usa para evitar NullPointerException cuando el registro no existe en BD
        var petFromDb = petRepository.findById(petId);

        if (petFromDb.isEmpty()) {
            // La excepción es interceptada por PetControllerAdvice y convertida en HTTP 404
            throw new PetNotFoundException("Pet con id " + petId + " no encontrado");
        }

        return mapToResponse(petFromDb.get());
    }

    // Marcar un Pet como disponible para adopción en el petstore
    public PetController.PetResponse markAvailable(Long petId) {
        log.info("Marcando Pet como disponible, petId={}", petId);

        var petFromDb = petRepository.findById(petId);
        if (petFromDb.isEmpty()) {
            throw new PetNotFoundException("Pet con id " + petId + " no encontrado");
        }

        var pet = petFromDb.get();
        pet.setAvailable(true);
        var saved = petRepository.save(pet);

        log.info("Pet marcado como disponible, id={}", saved.getId());
        return mapToResponse(saved);
    }

    // Obtener todos los Pets
    List<PetController.PetResponse> getAllPets() {
        log.info("Obteniendo todos los Pets de la BD");

        // findAll regresa una lista vacía si no hay registros — nunca regresa null
        // Stream y map: iteramos cada Entity y la transformamos a su DTO de respuesta
        return petRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Mapper privado: convierte una Entity a un DTO de respuesta
    // Entity: clase que representa una tabla en la BD con todos sus campos internos
    // DTO (Data Transfer Object): objeto que define solo los campos que el cliente debe ver en el response
    // Nunca se expone la Entity directamente al cliente
    private PetController.PetResponse mapToResponse(Pet pet) {
        return new PetController.PetResponse(
                pet.getId(),
                pet.getName(),
                pet.getColor(),
                pet.getRace(),
                pet.getAge(),
                pet.isAvailable());
    }
}
