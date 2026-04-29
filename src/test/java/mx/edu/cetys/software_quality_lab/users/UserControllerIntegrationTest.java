package mx.edu.cetys.software_quality_lab.users;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
        String body = """
            {
                "username": "juan4_dev",
                "firstName": "Juan",
                "lastName": "Pérez",
                "phone": "6641234567",
                "email": "hola#gm4l.com",
                "age": 12
            }""";
        mockMvc
            .perform(
                post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenPhoneIsInvalid() throws Exception {
        String body = """
            {
                "username": "juan4_dev",
                "firstName": "Juan",
                "lastName": "Pérez",
                "phone": "123",
                "email": "hola#gm4l.com",
                "age": 25
            }""";

        mockMvc
            .perform(
                post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenEmailIsInvalid() throws Exception {
        String body = """
            {
                "username": "juan4_dev",
                "firstName": "Juan",
                "lastName": "Pérez",
                "phone": "6641234567",
                "email": "user@gmail.com",
                "age": 25
            }""";

        mockMvc
            .perform(
                post("/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn409WhenUsernameIsDuplicated() throws Exception {
        userRepository.save(new User(
            "juan4_dev",
            "Juan",
            "Pérez",
            "6641234567",
            "hola#gm4l.com",
            25
        ));

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
            ).andExpect(status().isConflict());
    }

    // ─── GET /users/{id} ─────────────────────────────────────────────────────

    @Test
    void shouldReturn200AndUserWhenFound() throws Exception {
        User user = userRepository.save(new User(
                "juan4_dev",
                "Juan",
                "Pérez",
                "6641234567",
                "hola#gm4l.com",
                25
        ));

        mockMvc.perform(get("/users/"+user.getId())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
            .andExpect(jsonPath("$.response.user.username").value("juan4_dev"))
            .andExpect(jsonPath("$.response.user.firstName").value("Juan"))
            .andExpect(jsonPath("$.response.user.lastName").value("Pérez"))
            .andExpect(jsonPath("$.response.user.phone").value("6641234567"))
            .andExpect(jsonPath("$.response.user.email").value("hola#gm4l.com"))
            .andExpect(jsonPath("$.response.user.age").value(25))
            .andExpect(jsonPath("$.response.user.status").value("ACTIVE"));

    }

    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        mockMvc.perform(get("/users/9999")
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    // ─── PATCH /users/{id}/suspend ────────────────────────────────────────────

    @Test
    void shouldSuspendUserAndReturn200() throws Exception {
        User activeUser = new User(
                "juan4_dev",
                "Juan",
                "Pérez",
                "6641234567",
                "hola#gm4l.com",
                25);
        activeUser.setStatus(UserStatus.ACTIVE);

        User saved = userRepository.save(activeUser);

        mockMvc.perform(patch("/users/"+saved.getId()+"/suspend")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.response.user.status").value("SUSPENDED"));

    }

    @Test
    void shouldReturn400WhenSuspendingAlreadySuspendedUser() throws Exception {
        // TODO: guardar un usuario con status SUSPENDED via repository
        // TODO: realizar PATCH /users/{id}/suspend
        // TODO: andExpect status 400

        User suspendedUser = new User(
                "juan4_dev",
                "Juan",
                "Pérez",
                "6641234567",
                "hola#gm4l.com",
                25);
        suspendedUser.setStatus(UserStatus.SUSPENDED);

        User saved = userRepository.save(suspendedUser);

        mockMvc.perform(patch("/users/"+saved.getId()+"/suspend")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isBadRequest());

    }
}
