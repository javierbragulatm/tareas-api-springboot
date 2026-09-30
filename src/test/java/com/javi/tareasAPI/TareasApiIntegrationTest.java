package com.javi.tareasAPI;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TareasApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void registrarYLoginDeberianFuncionar() throws Exception {
        String registro = """
                {
                    "username": "integrationuser",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registro))
                .andExpect(status().isCreated());

        String login = """
                {
                    "username": "integrationuser",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(login))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String token = result.getResponse().getContentAsString();
                    assertFalse(token.isBlank());
                });
    }

    @Test
    void usuarioAutenticadoDeberiaPoderCrearYConsultarTarea() throws Exception {
        String token = obtenerToken("usuario1", "password123");

        String tarea = """
                {
                    "titulo": "Tarea de integración",
                    "completada": false
                }
                """;

        String respuesta = mockMvc.perform(post("/tareas")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(tarea))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Tarea de integración"))
                .andExpect(jsonPath("$.completada").value(false))
                .andReturn()
                .getResponse()
                .getContentAsString();

        int id = objectMapper.readTree(respuesta).get("id").asInt();

        mockMvc.perform(get("/tareas/" + id)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.titulo").value("Tarea de integración"));
    }

    @Test
    void usuarioNoAutenticadoNoDeberiaPoderAccederATareas() throws Exception {
        mockMvc.perform(get("/tareas"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void unUsuarioNoDeberiaPoderAccederALaTareaDeOtroUsuario() throws Exception {
        String tokenUsuario1 = obtenerToken("usuario1", "password123");
        String tokenUsuario2 = obtenerToken("usuario2", "password123");

        String tarea = """
                {
                    "titulo": "Tarea privada",
                    "completada": false
                }
                """;

        String respuesta = mockMvc.perform(post("/tareas")
                .header("Authorization", "Bearer " + tokenUsuario1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(tarea))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        int id = objectMapper.readTree(respuesta).get("id").asInt();

        mockMvc.perform(get("/tareas/" + id)
                .header("Authorization", "Bearer " + tokenUsuario2))
                .andExpect(status().isNotFound());
    }

    @Test
    void crearTareaConTituloInvalidoDeberiaDevolver400() throws Exception {
        String token = obtenerToken("usuario1", "password123");

        String tarea = """
                {
                    "titulo": "",
                    "completada": false
                }
                """;

        mockMvc.perform(post("/tareas")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(tarea))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.titulo").exists());
    }

    @Test
    void obtenerTareaInexistenteDeberiaDevolver404() throws Exception {
        String token = obtenerToken("usuario1", "password123");

        mockMvc.perform(get("/tareas/99999")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("No existe la tarea con id 99999"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void loginConCredencialesIncorrectasDeberiaDevolver401() throws Exception {
        String registro = """
                {
                    "username": "integrationuser",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registro))
                .andExpect(status().isCreated());

        String login = """
                {
                    "username": "integrationuser",
                    "password": "passwordincorrecta"
                }
                """;

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(login))
                .andExpect(status().isUnauthorized());
    }

    private String obtenerToken(String username, String password) throws Exception {
        String registro = """
                {
                    "username": "%s",
                    "password": "%s"
                }
                """.formatted(username, password);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registro))
                .andExpect(status().isCreated());

        String login = """
                {
                    "username": "%s",
                    "password": "%s"
                }
                """.formatted(username, password);

        return mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(login))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}

