package mx.edu.cetys.software_quality_lab.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    // Limpiar la BD antes de cada prueba para garantizar un estado independiente
    @BeforeEach
    public void limpiarBD() {
        userRepository.deleteAll();
    }

    // ─── POST /users ──────────────────────────────────────────────────────────

    @Test
    void shouldCreateUserAndReturn201() throws Exception {
        // El email sigue el formato del EmailValidatorService: usuario#proveedor.dominio
        String body = """
                {
                    "username": "juan4_dev",
                    "firstName": "Juan",
                    "lastName": "Pérez",
                    "phone": "6641234567",
                    "email": "hola#gm4l.com",
                    "age": 25
                }""";

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.response.user.username").value("juan4_dev"))
                .andExpect(jsonPath("$.response.user.firstName").value("Juan"))
                .andExpect(jsonPath("$.response.user.lastName").value("Pérez"))
                .andExpect(jsonPath("$.response.user.phone").value("6641234567"))
                .andExpect(jsonPath("$.response.user.email").value("hola#gm4l.com"))
                .andExpect(jsonPath("$.response.user.age").value(25))
                .andExpect(jsonPath("$.response.user.status").value("ACTIVE"));
    }

    @Test
    void shouldReturn400WhenUsernameIsTooShort() throws Exception {
        String body = """
                {
                    "username": "juan",
                    "firstName": "Juan",
                    "lastName": "Pérez",
                    "phone": "6641234567",
                    "email": "hola#gm4l.com",
                    "age": 25
                }""";
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenAgeIsExactlyTwelve() throws Exception {
        // TODO: body con age = 12 (caso límite — debe ser mayor a 12)
        // TODO: realizar POST /users
        // TODO: andExpect status 400
    }

    @Test
    void shouldReturn400WhenPhoneIsInvalid() throws Exception {
        // TODO: body con phone = "123" (menos de 10 dígitos)
        // TODO: realizar POST /users
        // TODO: andExpect status 400
    }

    @Test
    void shouldReturn400WhenEmailIsInvalid() throws Exception {
        // TODO: body con email en formato estándar "user@gmail.com" (no cumple las reglas del validador)
        // TODO: realizar POST /users
        // TODO: andExpect status 400
    }

    @Test
    void shouldReturn409WhenUsernameIsDuplicated() throws Exception {
        // TODO: guardar un usuario directamente via repository con el mismo username
        // TODO: realizar segundo POST /users con el mismo username
        // TODO: andExpect status 409
    }

    // ─── GET /users/{id} ─────────────────────────────────────────────────────

    @Test
    void shouldReturn200AndUserWhenFound() throws Exception {
        // TODO: guardar un usuario via repository, obtener su id generado
        // TODO: realizar GET /users/{id}
        // TODO: andExpect status 200
        // TODO: andExpect jsonPath campos coincidan con el usuario guardado
    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        // TODO: realizar GET /users/9999 (id inexistente)
        // TODO: andExpect status 404
    }

    // ─── PATCH /users/{id}/suspend ────────────────────────────────────────────

    @Test
    void shouldSuspendUserAndReturn200() throws Exception {
        // TODO: guardar un usuario ACTIVE via repository
        // TODO: realizar PATCH /users/{id}/suspend
        // TODO: andExpect status 200
        // TODO: andExpect jsonPath("$.response.user.status") == "SUSPENDED"
    }

    @Test
    void shouldReturn400WhenSuspendingAlreadySuspendedUser() throws Exception {
        // TODO: guardar un usuario con status SUSPENDED via repository
        // TODO: realizar PATCH /users/{id}/suspend
        // TODO: andExpect status 400
    }
}
