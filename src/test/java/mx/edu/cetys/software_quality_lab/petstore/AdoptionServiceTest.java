package mx.edu.cetys.software_quality_lab.petstore;

import mx.edu.cetys.software_quality_lab.petstore.exceptions.AdoptionNotFoundException;
import mx.edu.cetys.software_quality_lab.petstore.exceptions.MaxAdoptionsReachedException;
import mx.edu.cetys.software_quality_lab.petstore.exceptions.PetAlreadyAdoptedException;
import mx.edu.cetys.software_quality_lab.petstore.exceptions.UserNotEligibleException;
import mx.edu.cetys.software_quality_lab.pets.Pet;
import mx.edu.cetys.software_quality_lab.pets.PetRepository;
import mx.edu.cetys.software_quality_lab.pets.exceptions.PetNotFoundException;
import mx.edu.cetys.software_quality_lab.users.User;
import mx.edu.cetys.software_quality_lab.users.UserRepository;
import mx.edu.cetys.software_quality_lab.users.UserStatus;
import mx.edu.cetys.software_quality_lab.users.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdoptionServiceTest {

    @Mock
    AdoptionRepository adoptionRepository;

    @Mock
    UserRepository userRepository;

    // petstore reutiliza PetRepository del módulo pets/ — se mockea igual que cualquier otro repository
    @Mock
    PetRepository petRepository;

    @InjectMocks
    AdoptionService adoptionService;

    // Caso exitoso — crear adopción
    @Test
    void shouldCreateAdoptionSuccessfully() {
        // TODO: arrange — construir un usuario ACTIVE con edad 25, un pet con available=true,
        //       mockear userRepository.findById, petRepository.findById,
        //       adoptionRepository.existsByPetIdAndStatus = false,
        //       adoptionRepository.countByUserIdAndStatus = 0,
        //       adoptionRepository.save regresa una adopción con id
        // TODO: act — llamar a adoptionService.createAdoption(new AdoptionRequest(userId, petId))
        // TODO: assert — verificar id, userId, petId, status == "ACTIVE"; confirmar que save fue llamado una vez
    }

    // Caso exitoso — cancelar adopción
    @Test
    void shouldCancelAdoptionSuccessfully() {
        // TODO: arrange — mockear adoptionRepository.findById con una adopción ACTIVE
        //       mockear adoptionRepository.save para que regrese la adopción actualizada
        // TODO: act — llamar a adoptionService.cancelAdoption(adoptionId)
        // TODO: assert — verificar que el status sea "CANCELLED"; confirmar que pet.available vuelve a true
    }

    // Elegibilidad del usuario — no existe
    @Test
    void shouldThrowWhenUserDoesNotExist() {
        // TODO: mockear userRepository.findById para que regrese Optional.empty()
        // TODO: assertThrows UserNotFoundException
    }

    // Elegibilidad del usuario — suspendido
    @Test
    void shouldThrowWhenUserIsSuspended() {
        // TODO: mockear userRepository.findById con usuario SUSPENDED (edad 25)
        // TODO: assertThrows UserNotEligibleException
    }

    // Elegibilidad del usuario — menor de 18
    @Test
    void shouldThrowWhenUserIsTooYoungToAdopt() {
        // TODO: mockear userRepository.findById con usuario ACTIVE edad = 17
        // TODO: assertThrows UserNotEligibleException
    }

    // Elegibilidad del usuario — caso límite (boundary): exactamente 17 años
    @Test
    void shouldThrowWhenUserIsExactlySeventeen() {
        // TODO: edad 17, debe ser >= 18
        // TODO: assertThrows UserNotEligibleException
    }

    // Elegibilidad del pet — no existe
    @Test
    void shouldThrowWhenPetDoesNotExist() {
        // TODO: mockear usuario válido (ACTIVE, edad 25), mockear petRepository.findById para regresar Optional.empty()
        // TODO: assertThrows PetNotFoundException
    }

    // Elegibilidad del pet — ya tiene adopción activa
    @Test
    void shouldThrowWhenPetIsAlreadyAdopted() {
        // TODO: mockear usuario válido, mockear petRepository.findById con un pet,
        //       mockear adoptionRepository.existsByPetIdAndStatus(petId, ACTIVE) = true
        // TODO: assertThrows PetAlreadyAdoptedException
    }

    // Límite de adopciones — usuario con 3 ya activas
    @Test
    void shouldThrowWhenUserHasReachedMaxAdoptions() {
        // TODO: mockear usuario válido, pet válido, pet no adoptado,
        //       adoptionRepository.countByUserIdAndStatus = 3
        // TODO: assertThrows MaxAdoptionsReachedException
    }

    // Límite de adopciones — usuario con 2 activas (debe poder adoptar)
    @Test
    void shouldAllowAdoptionWhenUserHasTwoActiveAdoptions() {
        // TODO: igual que el caso exitoso pero countByUserIdAndStatus = 2
        // TODO: NO debe lanzar excepción — verificar que save fue llamado
    }

    // Cancelar adopción — no existe
    @Test
    void shouldThrowWhenCancellingNonExistentAdoption() {
        // TODO: mockear adoptionRepository.findById para que regrese Optional.empty()
        // TODO: assertThrows AdoptionNotFoundException
    }

    // Cancelar adopción — ya cancelada
    @Test
    void shouldThrowWhenCancellingAlreadyCancelledAdoption() {
        // TODO: mockear adoptionRepository.findById con una adopción CANCELLED
        // TODO: assertThrows (InvalidUserDataException o excepción dedicada)
    }
}
