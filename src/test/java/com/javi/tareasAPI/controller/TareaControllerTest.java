package com.javi.tareasAPI.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javi.tareasAPI.dto.TareaDTO;
import com.javi.tareasAPI.dto.TareaRequestDTO;
import com.javi.tareasAPI.security.JwtService;
import com.javi.tareasAPI.service.TareaService;

@WebMvcTest(TareaController.class)
public class TareaControllerTest {

    @org.springframework.beans.factory.annotation.Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private TareaService service;

    @MockitoBean
    private JwtService jwtService;

    @Test
    public void crearTarea_deberiaCrearTarea() throws Exception {

        TareaRequestDTO request = new TareaRequestDTO();
        request.setTitulo("Spring Boot");
        request.setCompletada(false);

        TareaDTO respuesta =
                new TareaDTO(1, "Spring Boot", false);

        when(service.crearTarea(any(TareaRequestDTO.class)))
                .thenReturn(respuesta);

        mockMvc.perform(
                post("/tareas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.titulo").value("Spring Boot"))
        .andExpect(jsonPath("$.completada").value(false))
        .andExpect(jsonPath("$.usuario").doesNotExist());
    }

    @Test
    public void crearTarea_deberiaRechazarTituloVacio() throws Exception {

        TareaRequestDTO request = new TareaRequestDTO();
        request.setTitulo("");
        request.setCompletada(false);

        mockMvc.perform(
                post("/tareas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.titulo").value(
                "El titulo no puede estar vacio"
        ));
    }

    @Test
    public void crearTarea_deberiaRechazarTituloDemasiadoCorto()
            throws Exception {

        TareaRequestDTO request = new TareaRequestDTO();
        request.setTitulo("AB");
        request.setCompletada(false);

        mockMvc.perform(
                post("/tareas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.titulo").value(
                "El titulo debe tener entre 3 y 100 caracteres"
        ));
    }

    @Test
    public void obtenerTarea_deberiaDevolverTarea() throws Exception {

        TareaDTO tarea =
                new TareaDTO(1, "Docker", false);

        when(service.obtenerTarea(1))
                .thenReturn(tarea);

        mockMvc.perform(
                get("/tareas/1")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.titulo").value("Docker"))
        .andExpect(jsonPath("$.completada").value(false))
        .andExpect(jsonPath("$.usuario").doesNotExist());
    }

    @Test
    public void updateTarea_deberiaActualizarTarea() throws Exception {

        TareaRequestDTO request = new TareaRequestDTO();
        request.setTitulo("Docker actualizado");
        request.setCompletada(true);

        TareaDTO respuesta =
                new TareaDTO(1, "Docker actualizado", true);

        when(service.updateTarea(
                eq(1),
                any(TareaRequestDTO.class)
        )).thenReturn(respuesta);

        mockMvc.perform(
                put("/tareas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.titulo").value("Docker actualizado"))
        .andExpect(jsonPath("$.completada").value(true))
        .andExpect(jsonPath("$.usuario").doesNotExist());
    }

    @Test
    public void updateTarea_deberiaRechazarTituloVacio() throws Exception {

        TareaRequestDTO request = new TareaRequestDTO();
        request.setTitulo("");
        request.setCompletada(false);

        mockMvc.perform(
                put("/tareas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.titulo").value(
                "El titulo no puede estar vacio"
        ));
    }

    @Test
    public void deleteTarea_deberiaDevolverNoContent() throws Exception {

        mockMvc.perform(
                delete("/tareas/1")
        )
        .andExpect(status().isNoContent());
    }

    @Test
    public void obtenerCompletadas_deberiaDevolverDTOs() throws Exception {

        List<TareaDTO> tareas = List.of(
                new TareaDTO(1, "Spring", true),
                new TareaDTO(2, "Docker", true)
        );

        when(service.obtenerCompletadas())
                .thenReturn(tareas);

        mockMvc.perform(
                get("/tareas/completadas")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].titulo").value("Spring"))
        .andExpect(jsonPath("$[0].completada").value(true))
        .andExpect(jsonPath("$[0].usuario").doesNotExist())
        .andExpect(jsonPath("$[1].id").value(2))
        .andExpect(jsonPath("$[1].titulo").value("Docker"));
    }

    @Test
    public void obtenerPendientes_deberiaDevolverDTOs() throws Exception {

        List<TareaDTO> tareas = List.of(
                new TareaDTO(3, "Java", false)
        );

        when(service.obtenerPendientes())
                .thenReturn(tareas);

        mockMvc.perform(
                get("/tareas/pendientes")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(3))
        .andExpect(jsonPath("$[0].titulo").value("Java"))
        .andExpect(jsonPath("$[0].completada").value(false))
        .andExpect(jsonPath("$[0].usuario").doesNotExist());
    }

    @Test
    public void buscarPorTitulo_deberiaDevolverDTOs() throws Exception {

        List<TareaDTO> tareas = List.of(
                new TareaDTO(1, "Spring Boot", false)
        );

        when(service.buscarPorTitulo("Spring"))
                .thenReturn(tareas);

        mockMvc.perform(
                get("/tareas/buscar/Spring")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].titulo").value("Spring Boot"))
        .andExpect(jsonPath("$[0].completada").value(false))
        .andExpect(jsonPath("$[0].usuario").doesNotExist());
    }

    @Test
    public void buscarConFiltros_deberiaDevolverPagina() throws Exception {

        TareaDTO tarea =
                new TareaDTO(1, "Spring Boot", false);

        PageImpl<TareaDTO> pagina =
                new PageImpl<>(
                        List.of(tarea),
                        PageRequest.of(0, 5),
                        1
                );

        when(service.buscarConFiltros(
                eq("Spring"),
                eq(false),
                eq(0),
                eq(5),
                eq("titulo"),
                eq("asc")
        )).thenReturn(pagina);

        mockMvc.perform(
                get("/tareas/filtrar")
                        .param("texto", "Spring")
                        .param("completada", "false")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sort", "titulo")
                        .param("direction", "asc")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].id").value(1))
        .andExpect(jsonPath("$.content[0].titulo").value("Spring Boot"))
        .andExpect(jsonPath("$.content[0].completada").value(false))
        .andExpect(jsonPath("$.content[0].usuario").doesNotExist());
    }
}
