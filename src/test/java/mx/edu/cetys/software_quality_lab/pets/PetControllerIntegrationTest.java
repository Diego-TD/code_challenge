package mx.edu.cetys.software_quality_lab.pets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @SpringBootTest levanta el contexto completo de Spring con la BD H2 en memoria
// @AutoConfigureMockMvc inyecta MockMvc: un cliente HTTP simulado (equivalente a Postman pero en código)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // Inyectamos el repository directamente para preparar datos de prueba sin pasar por el API
    @Autowired
    private PetRepository petRepository;

    // @BeforeEach se ejecuta antes de CADA prueba — garantiza que la BD inicia vacía en cada test
    @BeforeEach
    public void limpiarBD() {
        petRepository.deleteAll();
    }

    // Helper: guardar un pet directo en BD para usarlo en pruebas de GET
    private Pet guardarPetEnBD(String name, String race, String color, Integer age) {
        return petRepository.save(new Pet(name, race, color, age));
    }

    // POST /pets — crear un nuevo Pet exitosamente
    @Test
    void shouldCreatePetAndReturn201() throws Exception {
        String body = """
                {
                    "name": "pop",
                    "race": "dalmata",
                    "color": "blanco y negro",
                    "age": 1
                }""";

        mockMvc.perform(
                        post("/pets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isCreated())                                    // HTTP 201
                .andExpect(jsonPath("$.info").value("Pet creado exitosamente"))
                .andExpect(jsonPath("$.response.pet.name").value("pop"))
                .andExpect(jsonPath("$.response.pet.color").value("blanco y negro"))
                .andExpect(jsonPath("$.response.pet.race").value("dalmata"))
                .andExpect(jsonPath("$.response.pet.age").value(1))
                .andExpect(jsonPath("$.response.pet.id").isNumber())                // el ID fue generado por la BD
                .andExpect(jsonPath("$.error").isEmpty());
    }

    // POST /pets — edad 0 es válida (caso límite)
    @Test
    void shouldReturn201WhenAgeIsZero() throws Exception {
        String body = """
                { "name": "Bebé", "race": "Poodle", "color": "Blanco", "age": 0 }""";

        mockMvc.perform(post("/pets").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.response.pet.age").value(0));
    }

    // POST /pets — nombre de 1 carácter debe regresar 400
    @Test
    void shouldReturn400WhenNameIsTooShort() throws Exception {
        String body = """
                { "name": "L", "race": "Labrador", "color": "Negro", "age": 3 }""";

        mockMvc.perform(post("/pets").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())                                 // HTTP 400
                .andExpect(jsonPath("$.error").isNotEmpty());
    }

    // POST /pets — nombre con solo espacios debe regresar 400
    @Test
    void shouldReturn400WhenNameIsBlank() throws Exception {
        String body = """
                { "name": "   ", "race": "Labrador", "color": "Negro", "age": 3 }""";

        mockMvc.perform(post("/pets").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    // POST /pets — nombre ausente del JSON (null) debe regresar 400
    @Test
    void shouldReturn400WhenNameIsNull() throws Exception {
        String body = """
                { "race": "Labrador", "color": "Negro", "age": 3 }""";

        mockMvc.perform(post("/pets").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    // POST /pets — raza vacía debe regresar 400
    @Test
    void shouldReturn400WhenRaceIsBlank() throws Exception {
        String body = """
                { "name": "Luna", "race": "", "color": "Negro", "age": 3 }""";

        mockMvc.perform(post("/pets").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    // POST /pets — raza ausente del JSON debe regresar 400
    @Test
    void shouldReturn400WhenRaceIsNull() throws Exception {
        String body = """
                { "name": "Luna", "color": "Negro", "age": 3 }""";

        mockMvc.perform(post("/pets").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    // POST /pets — color con solo espacios debe regresar 400
    @Test
    void shouldReturn400WhenColorIsBlank() throws Exception {
        String body = """
                { "name": "Luna", "race": "Labrador", "color": "   ", "age": 3 }""";

        mockMvc.perform(post("/pets").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    // POST /pets — color ausente del JSON debe regresar 400
    @Test
    void shouldReturn400WhenColorIsNull() throws Exception {
        String body = """
                { "name": "Luna", "race": "Labrador", "age": 3 }""";

        mockMvc.perform(post("/pets").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    // POST /pets — edad negativa debe regresar 400 (caso límite: -1 no válido, 0 sí)
    @Test
    void shouldReturn400WhenAgeIsNegative() throws Exception {
        String body = """
                { "name": "Luna", "race": "Labrador", "color": "Negro", "age": -1 }""";

        mockMvc.perform(post("/pets").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    // POST /pets — edad ausente del JSON debe regresar 400
    @Test
    void shouldReturn400WhenAgeIsNull() throws Exception {
        String body = """
                { "name": "Luna", "race": "Labrador", "color": "Negro" }""";

        mockMvc.perform(post("/pets").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    // GET /pets/{id} — pet existe en BD, debe regresar 200 con datos correctos
    @Test
    void shouldReturn200WhenPetExists() throws Exception {
        // Guardar un pet directo en BD y usar el ID generado para el GET
        var pet = guardarPetEnBD("Rocky", "Bulldog", "Cafe", 4);

        mockMvc.perform(get("/pets/" + pet.getId()))
                .andExpect(status().isOk())                                         // HTTP 200
                .andExpect(jsonPath("$.response.pet.name").value("Rocky"))
                .andExpect(jsonPath("$.response.pet.race").value("Bulldog"))
                .andExpect(jsonPath("$.response.pet.color").value("Cafe"))
                .andExpect(jsonPath("$.response.pet.age").value(4))
                .andExpect(jsonPath("$.response.pet.id").value(pet.getId()));
    }

    // GET /pets/{id} — ID inexistente debe regresar 404
    @Test
    void shouldReturn404WhenPetNotFound() throws Exception {
        // La BD está vacía (limpiada en @BeforeEach)
        mockMvc.perform(get("/pets/9999"))
                .andExpect(status().isNotFound())                                   // HTTP 404
                .andExpect(jsonPath("$.error").isNotEmpty());
    }

    // GET /pets/{id} — ID igual a 0 debe regresar 400 (no es un ID válido)
    @Test
    void shouldReturn400WhenPetIdIsZero() throws Exception {
        mockMvc.perform(get("/pets/0"))
                .andExpect(status().isBadRequest());                                // HTTP 400
    }

    // GET /pets/{id} — ID negativo debe regresar 400
    @Test
    void shouldReturn400WhenPetIdIsNegative() throws Exception {
        mockMvc.perform(get("/pets/-1"))
                .andExpect(status().isBadRequest());
    }

    // GET /pets — regresa todos los pets guardados en BD
    @Test
    void shouldReturn200WithAllPets() throws Exception {
        guardarPetEnBD("Luna", "Labrador", "Negro", 3);
        guardarPetEnBD("Max", "Husky", "Blanco", 5);

        mockMvc.perform(get("/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").isArray())
                .andExpect(jsonPath("$.response.length()").value(2));
    }

    // GET /pets — BD vacía debe regresar lista vacía (no un error)
    @Test
    void shouldReturn200WithEmptyListWhenNoPetsExist() throws Exception {
        mockMvc.perform(get("/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").isArray())
                .andExpect(jsonPath("$.response.length()").value(0));
    }
}
