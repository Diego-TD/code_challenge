package mx.edu.cetys.software_quality_lab.pets;

import mx.edu.cetys.software_quality_lab.pets.exceptions.InvalidPetDataException;
import mx.edu.cetys.software_quality_lab.pets.exceptions.PetNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class) activa Mockito para esta clase
// Permite usar @Mock e @InjectMocks sin levantar el contexto completo de Spring (más rápido)
@ExtendWith(MockitoExtension.class)
public class PetServiceTest {

    // @Mock crea un "doble" del PetRepository — simula sus métodos sin tocar la BD real
    @Mock
    PetRepository petRepository;

    // @InjectMocks crea una instancia real de PetService e inyecta los @Mock automáticamente
    @InjectMocks
    PetService petService;

    // Helper para construir un Pet simulado como si ya estuviera guardado en BD (con ID asignado)
    private Pet buildMockSavedPet(Long id, String name, String race, String color, Integer age) {
        var pet = new Pet(name, race, color, age);
        pet.setId(id);
        return pet;
    }

    // savePet — caso exitoso
    @Test
    void savePet_exitoso() {
        // Arrange — preparar los datos de entrada y configurar el comportamiento del mock
        var request = new PetController.PetRequest("Luna", "Negro", "Labrador", 3);
        var mockSaved = buildMockSavedPet(1L, "Luna", "Labrador", "Negro", 3);

        // when(...).thenReturn(...): cuando se llame a petRepository.save con cualquier argumento,
        // simular que regresa el pet con ID ya asignado por la BD
        when(petRepository.save(any())).thenReturn(mockSaved);

        // Act — ejecutar el método que estamos probando
        var response = petService.savePet(request);

        // Assert — verificar el resultado y que el repository fue llamado correctamente
        verify(petRepository, times(1)).save(any()); // se llamó exactamente una vez
        assertEquals(1L, response.id());
        assertEquals("Luna", response.name());
        assertEquals("Labrador", response.race());
        assertEquals("Negro", response.color());
        assertEquals(3, response.age());
    }

    // savePet — edad 0 es válida (cachorro recién nacido)
    @Test
    void savePet_edadCero_exitoso() {
        var request = new PetController.PetRequest("Max", "Blanco", "Husky", 0);
        var mockSaved = buildMockSavedPet(2L, "Max", "Husky", "Blanco", 0);
        when(petRepository.save(any())).thenReturn(mockSaved);

        var response = petService.savePet(request);

        assertEquals(0, response.age());
        verify(petRepository, times(1)).save(any());
    }

    // savePet — validación de Nombre: nulo
    @Test
    void savePet_nombreNulo_excepcionEsperada() {
        var request = new PetController.PetRequest(null, "Negro", "Labrador", 3);

        assertThrows(InvalidPetDataException.class, () -> petService.savePet(request));

        // never(): verificar que el repository NO fue llamado si hubo un error de validación
        verify(petRepository, never()).save(any());
    }

    // savePet — validación de Nombre: vacío
    @Test
    void savePet_nombreVacio_excepcionEsperada() {
        var request = new PetController.PetRequest("", "Negro", "Labrador", 3);
        assertThrows(InvalidPetDataException.class, () -> petService.savePet(request));
        verify(petRepository, never()).save(any());
    }

    // savePet — validación de Nombre: solo espacios en blanco (isBlank lo detecta)
    @Test
    void savePet_nombreSoloEspacios_excepcionEsperada() {
        var request = new PetController.PetRequest("   ", "Negro", "Labrador", 3);
        assertThrows(InvalidPetDataException.class, () -> petService.savePet(request));
        verify(petRepository, never()).save(any());
    }

    // savePet — validación de Nombre: un solo carácter (caso límite / boundary)
    @Test
    void savePet_nombreMenosDeDosCaracteres_excepcionEsperada() {
        var request = new PetController.PetRequest("L", "Negro", "Labrador", 3);
        assertThrows(InvalidPetDataException.class, () -> petService.savePet(request));
        verify(petRepository, never()).save(any());
    }

    // savePet — validación de Raza: nula
    @Test
    void savePet_razaNula_excepcionEsperada() {
        var request = new PetController.PetRequest("Luna", "Negro", null, 3);
        assertThrows(InvalidPetDataException.class, () -> petService.savePet(request));
        verify(petRepository, never()).save(any());
    }

    // savePet — validación de Raza: vacía
    @Test
    void savePet_razaVacia_excepcionEsperada() {
        var request = new PetController.PetRequest("Luna", "Negro", "", 3);
        assertThrows(InvalidPetDataException.class, () -> petService.savePet(request));
        verify(petRepository, never()).save(any());
    }

    // savePet — validación de Raza: solo espacios
    @Test
    void savePet_razaSoloEspacios_excepcionEsperada() {
        var request = new PetController.PetRequest("Luna", "Negro", "   ", 3);
        assertThrows(InvalidPetDataException.class, () -> petService.savePet(request));
        verify(petRepository, never()).save(any());
    }

    // savePet — validación de Color: nulo
    @Test
    void savePet_colorNulo_excepcionEsperada() {
        var request = new PetController.PetRequest("Luna", null, "Labrador", 3);
        assertThrows(InvalidPetDataException.class, () -> petService.savePet(request));
        verify(petRepository, never()).save(any());
    }

    // savePet — validación de Color: vacío
    @Test
    void savePet_colorVacio_excepcionEsperada() {
        var request = new PetController.PetRequest("Luna", "", "Labrador", 3);
        assertThrows(InvalidPetDataException.class, () -> petService.savePet(request));
        verify(petRepository, never()).save(any());
    }

    // savePet — validación de Color: solo espacios
    @Test
    void savePet_colorSoloEspacios_excepcionEsperada() {
        var request = new PetController.PetRequest("Luna", "   ", "Labrador", 3);
        assertThrows(InvalidPetDataException.class, () -> petService.savePet(request));
        verify(petRepository, never()).save(any());
    }

    // savePet — validación de Edad: nula
    @Test
    void savePet_edadNula_excepcionEsperada() {
        var request = new PetController.PetRequest("Luna", "Negro", "Labrador", null);
        assertThrows(InvalidPetDataException.class, () -> petService.savePet(request));
        verify(petRepository, never()).save(any());
    }

    // savePet — validación de Edad: negativa (caso límite: -1 no válido, 0 sí)
    @Test
    void savePet_edadNegativa_excepcionEsperada() {
        var request = new PetController.PetRequest("Luna", "Negro", "Labrador", -1);
        assertThrows(InvalidPetDataException.class, () -> petService.savePet(request));
        verify(petRepository, never()).save(any());
    }

    // getPetById — caso exitoso
    @Test
    void getPetById_exitoso() {
        // Arrange — simular que el repository encuentra el pet con id=1
        // Optional.of(...) simula que el registro SÍ existe en la BD
        var mockPet = buildMockSavedPet(1L, "Luna", "Labrador", "Negro", 3);
        when(petRepository.findById(1L)).thenReturn(Optional.of(mockPet));

        var response = petService.getPetById(1L);

        verify(petRepository, times(1)).findById(1L);
        assertEquals(1L, response.id());
        assertEquals("Luna", response.name());
        assertEquals("Labrador", response.race());
        assertEquals("Negro", response.color());
        assertEquals(3, response.age());
    }

    // getPetById — ID nulo: no debe llegar a consultar la BD
    @Test
    void getPetById_idNulo_excepcionEsperada() {
        assertThrows(InvalidPetDataException.class, () -> petService.getPetById(null));
        verify(petRepository, never()).findById(any());
    }

    // getPetById — ID cero (caso límite: 0 no es un ID válido en BD)
    @Test
    void getPetById_idCero_excepcionEsperada() {
        assertThrows(InvalidPetDataException.class, () -> petService.getPetById(0L));
        verify(petRepository, never()).findById(any());
    }

    // getPetById — ID negativo
    @Test
    void getPetById_idNegativo_excepcionEsperada() {
        assertThrows(InvalidPetDataException.class, () -> petService.getPetById(-5L));
        verify(petRepository, never()).findById(any());
    }

    // getPetById — Pet no encontrado en BD (404)
    // Optional.empty() simula que el registro NO existe en la BD
    @Test
    void getPetById_noEncontrado_excepcionEsperada() {
        when(petRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PetNotFoundException.class, () -> petService.getPetById(99L));
        verify(petRepository, times(1)).findById(99L);
    }

    // getAllPets — regresa lista con pets cuando hay datos en BD
    @Test
    void getAllPets_conPets_regriesaLista() {
        var pet1 = buildMockSavedPet(1L, "Luna", "Labrador", "Negro", 3);
        var pet2 = buildMockSavedPet(2L, "Max", "Husky", "Blanco", 5);
        when(petRepository.findAll()).thenReturn(List.of(pet1, pet2));

        var pets = petService.getAllPets();

        verify(petRepository, times(1)).findAll();
        assertEquals(2, pets.size());
        assertEquals("Luna", pets.get(0).name());
        assertEquals("Max", pets.get(1).name());
    }

    // getAllPets — regresa lista vacía cuando no hay datos (no debe lanzar excepción)
    @Test
    void getAllPets_sinPets_regriesaListaVacia() {
        when(petRepository.findAll()).thenReturn(List.of());

        var pets = petService.getAllPets();

        verify(petRepository, times(1)).findAll();
        assertTrue(pets.isEmpty());
    }
}
