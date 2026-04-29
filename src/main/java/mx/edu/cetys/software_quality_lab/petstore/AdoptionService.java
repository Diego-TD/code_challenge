package mx.edu.cetys.software_quality_lab.petstore;

import mx.edu.cetys.software_quality_lab.pets.PetRepository;
import mx.edu.cetys.software_quality_lab.pets.exceptions.PetNotFoundException;
import mx.edu.cetys.software_quality_lab.users.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdoptionService {

    private final Logger log = LoggerFactory.getLogger(AdoptionService.class);

    private final AdoptionRepository adoptionRepository;
    private final UserRepository userRepository;
    // petstore reutiliza el PetRepository del módulo pets/ — no duplica entidades
    private final PetRepository petRepository;

    public AdoptionService(AdoptionRepository adoptionRepository,
                           UserRepository userRepository,
                           PetRepository petRepository) {
        this.adoptionRepository = adoptionRepository;
        this.userRepository = userRepository;
        this.petRepository = petRepository;
    }

    /**
     * Listar todos los pets disponibles para adopción (available = true).
     */
    List<AdoptionController.AvailablePetResponse> listAvailablePets() {
        log.info("Obteniendo pets disponibles para adopción");
        // TODO: llamar a petRepository.findAllByAvailableTrue(), mapear cada Pet a AvailablePetResponse
        throw new UnsupportedOperationException("TODO: implementar listAvailablePets");
    }

    /**
     * Crear una adopción vinculando un usuario con un pet.
     *
     * Reglas a implementar (usar la excepción indicada en cada caso):
     *  1. El usuario debe existir                              → UserNotFoundException
     *  2. El status del usuario debe ser ACTIVE                → UserNotEligibleException
     *  3. La edad del usuario debe ser >= 18                   → UserNotEligibleException
     *  4. El pet debe existir                                  → PetNotFoundException
     *  5. El pet no debe tener una adopción ACTIVE             → PetAlreadyAdoptedException
     *  6. El usuario debe tener menos de 3 adopciones ACTIVE   → MaxAdoptionsReachedException
     *
     * En caso de éxito: marcar pet.available = false, guardar el pet, guardar la adopción y regresar respuesta.
     */
    AdoptionController.AdoptionResponse createAdoption(AdoptionController.AdoptionRequest request) {
        log.info("Creando adopción, userId={}, petId={}", request.userId(), request.petId());
        // TODO: implementar todas las reglas anteriores, persistir y mapear la respuesta
        throw new UnsupportedOperationException("TODO: implementar createAdoption");
    }

    /**
     * Cancelar una adopción existente.
     *
     * Reglas a implementar:
     *  1. La adopción debe existir        → AdoptionNotFoundException
     *  2. La adopción debe estar ACTIVE   → InvalidUserDataException
     *
     * En caso de éxito: cambiar adoption.status = CANCELLED, marcar pet.available = true, guardar ambos.
     */
    AdoptionController.AdoptionResponse cancelAdoption(Long adoptionId) {
        log.info("Cancelando adopción, adoptionId={}", adoptionId);
        // TODO: implementar las reglas anteriores, persistir los cambios y mapear la respuesta
        throw new UnsupportedOperationException("TODO: implementar cancelAdoption");
    }

    private AdoptionController.AdoptionResponse mapToResponse(Adoption adoption) {
        // TODO: mapear los campos de la Entity Adoption al record AdoptionController.AdoptionResponse
        throw new UnsupportedOperationException("TODO: implementar mapToResponse");
    }
}
