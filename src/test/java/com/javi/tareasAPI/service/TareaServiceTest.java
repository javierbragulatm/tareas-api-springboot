package com.javi.tareasAPI.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.javi.tareasAPI.dto.TareaDTO;
import com.javi.tareasAPI.dto.TareaRequestDTO;
import com.javi.tareasAPI.exception.TareaNoEncontradaException;
import com.javi.tareasAPI.model.Tarea;
import com.javi.tareasAPI.model.Usuario;
import com.javi.tareasAPI.repository.TareaRepository;
import com.javi.tareasAPI.repository.UsuarioRepository;

public class TareaServiceTest {

    @Mock
    private TareaRepository repository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private TareaService service;

    public TareaServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    private void autenticarUsuario() {

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "javi",
                        null
                )
        );
    }

    @AfterEach
    public void limpiarSeguridad() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void obtenerTareaExistente() {

        autenticarUsuario();

        Tarea tarea = new Tarea(1, "Spring", false);

        when(repository.findByIdAndUsuarioUsername(1, "javi"))
                .thenReturn(Optional.of(tarea));

        TareaDTO resultado = service.obtenerTarea(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Spring", resultado.getTitulo());
        assertFalse(resultado.isCompletada());
    }

    @Test
    public void crearTarea() {

        autenticarUsuario();

        TareaRequestDTO tareaDTO = new TareaRequestDTO();
        tareaDTO.setTitulo("Docker");
        tareaDTO.setCompletada(false);

        Usuario usuario = new Usuario();
        usuario.setUsername("javi");

        when(usuarioRepository.findByUsername("javi"))
                .thenReturn(Optional.of(usuario));

        when(repository.save(any(Tarea.class)))
                .thenAnswer(invocation -> {
                    Tarea tarea = invocation.getArgument(0);
                    tarea.setId(1);
                    return tarea;
                });

        TareaDTO resultado = service.crearTarea(tareaDTO);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Docker", resultado.getTitulo());
        assertFalse(resultado.isCompletada());

        verify(usuarioRepository)
                .findByUsername("javi");

        verify(repository)
                .save(any(Tarea.class));
    }

    @Test
    public void crearTarea_deberiaAsignarUsuarioActual() {

        autenticarUsuario();

        TareaRequestDTO tareaDTO = new TareaRequestDTO();
        tareaDTO.setTitulo("Docker");
        tareaDTO.setCompletada(false);

        Usuario usuario = new Usuario();
        usuario.setUsername("javi");

        when(usuarioRepository.findByUsername("javi"))
                .thenReturn(Optional.of(usuario));

        when(repository.save(any(Tarea.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        TareaDTO resultado = service.crearTarea(tareaDTO);

        assertNotNull(resultado);
        assertEquals("Docker", resultado.getTitulo());

        ArgumentCaptor<Tarea> captor =
                ArgumentCaptor.forClass(Tarea.class);

        verify(repository).save(captor.capture());

        Tarea tareaGuardada = captor.getValue();

        assertEquals(usuario, tareaGuardada.getUsuario());
        assertEquals("Docker", tareaGuardada.getTitulo());
        assertFalse(tareaGuardada.isCompletada());
    }

    @Test
    public void borrarTarea() {

        autenticarUsuario();

        Tarea tarea = new Tarea(5, "Spring", false);

        when(repository.findByIdAndUsuarioUsername(5, "javi"))
                .thenReturn(Optional.of(tarea));

        service.deleteTarea(5);

        verify(repository).delete(tarea);
    }

    @Test
    public void deleteTarea_deberiaRechazarTareaDeOtroUsuario() {

        autenticarUsuario();

        when(repository.findByIdAndUsuarioUsername(5, "javi"))
                .thenReturn(Optional.empty());

        assertThrows(
                TareaNoEncontradaException.class,
                () -> service.deleteTarea(5)
        );

        verify(repository, never())
                .delete(any(Tarea.class));
    }

    @Test
    public void obtenerCompletadas() {

        autenticarUsuario();

        service.obtenerCompletadas();

        verify(repository)
                .findByUsuarioUsernameAndCompletada(
                        "javi",
                        true
                );
    }

    @Test
    public void obtenerPendientes() {

        autenticarUsuario();

        service.obtenerPendientes();

        verify(repository)
                .findByUsuarioUsernameAndCompletada(
                        "javi",
                        false
                );
    }

    @Test
    public void obtenerTareas_deberiaDevolverSoloTareasDelUsuario() {

        autenticarUsuario();

        Tarea tarea1 = new Tarea(1, "Spring", false);
        Tarea tarea2 = new Tarea(2, "Docker", true);

        when(repository.findByUsuarioUsername("javi"))
                .thenReturn(List.of(tarea1, tarea2));

        List<TareaDTO> resultado = service.obtenerTareas();

        assertEquals(2, resultado.size());

        assertEquals("Spring", resultado.get(0).getTitulo());
        assertFalse(resultado.get(0).isCompletada());

        assertEquals("Docker", resultado.get(1).getTitulo());
        assertTrue(resultado.get(1).isCompletada());

        verify(repository)
                .findByUsuarioUsername("javi");
    }

    @Test
    public void obtenerTarea_deberiaRechazarTareaDeOtroUsuario() {

        autenticarUsuario();

        when(repository.findByIdAndUsuarioUsername(5, "javi"))
                .thenReturn(Optional.empty());

        assertThrows(
                TareaNoEncontradaException.class,
                () -> service.obtenerTarea(5)
        );
    }

    @Test
    public void updateTarea_deberiaRechazarTareaDeOtroUsuario() {

        autenticarUsuario();

        TareaRequestDTO tareaActualizada =
                new TareaRequestDTO();

        tareaActualizada.setTitulo("Nuevo titulo");
        tareaActualizada.setCompletada(true);

        when(repository.findByIdAndUsuarioUsername(5, "javi"))
                .thenReturn(Optional.empty());

        assertThrows(
                TareaNoEncontradaException.class,
                () -> service.updateTarea(
                        5,
                        tareaActualizada
                )
        );

        verify(repository, never())
                .save(any(Tarea.class));
    }

    @Test
    public void updateTarea_deberiaActualizarTarea() {

        autenticarUsuario();

        Tarea tareaExistente =
                new Tarea(5, "Titulo antiguo", false);

        TareaRequestDTO tareaActualizada =
                new TareaRequestDTO();

        tareaActualizada.setTitulo("Nuevo titulo");
        tareaActualizada.setCompletada(true);

        when(repository.findByIdAndUsuarioUsername(5, "javi"))
                .thenReturn(Optional.of(tareaExistente));

        when(repository.save(tareaExistente))
                .thenReturn(tareaExistente);

        TareaDTO resultado =
                service.updateTarea(
                        5,
                        tareaActualizada
                );

        assertNotNull(resultado);
        assertEquals(5, resultado.getId());
        assertEquals("Nuevo titulo", resultado.getTitulo());
        assertTrue(resultado.isCompletada());

        verify(repository)
                .save(tareaExistente);
    }

    @Test
    public void buscarConFiltros_deberiaFiltrarPorTextoYEstado() {

        autenticarUsuario();

        Tarea tarea1 =
                new Tarea(1, "Spring Boot", false);

        Tarea tarea2 =
                new Tarea(2, "Spring Security", false);

        Page<Tarea> pagina =
                new PageImpl<>(
                        List.of(tarea1, tarea2)
                );

        when(repository.buscarConFiltros(
                eq("javi"),
                eq("Spring"),
                eq(false),
                any(Pageable.class)
        )).thenReturn(pagina);

        Page<TareaDTO> resultado =
                service.buscarConFiltros(
                        "Spring",
                        false,
                        0,
                        5,
                        "titulo",
                        "asc"
                );

        assertNotNull(resultado);
        assertEquals(2, resultado.getContent().size());

        assertEquals(
                "Spring Boot",
                resultado.getContent().get(0).getTitulo()
        );

        assertEquals(
                "Spring Security",
                resultado.getContent().get(1).getTitulo()
        );

        verify(repository)
                .buscarConFiltros(
                        eq("javi"),
                        eq("Spring"),
                        eq(false),
                        any(Pageable.class)
                );
    }

    @Test
    public void buscarConFiltros_deberiaPermitirPaginacionYOrdenacion() {

        autenticarUsuario();

        Page<Tarea> pagina =
                new PageImpl<>(
                        List.of(
                                new Tarea(1, "Docker", false)
                        )
                );

        when(repository.buscarConFiltros(
                eq("javi"),
                isNull(),
                isNull(),
                any(Pageable.class)
        )).thenReturn(pagina);

        Page<TareaDTO> resultado =
                service.buscarConFiltros(
                        null,
                        null,
                        1,
                        10,
                        "titulo",
                        "desc"
                );

        assertEquals(1, resultado.getContent().size());

        ArgumentCaptor<Pageable> captor =
                ArgumentCaptor.forClass(Pageable.class);

        verify(repository).buscarConFiltros(
                eq("javi"),
                isNull(),
                isNull(),
                captor.capture()
        );

        Pageable pageable = captor.getValue();

        assertEquals(1, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());

        assertEquals(
                "titulo",
                pageable.getSort()
                        .getOrderFor("titulo")
                        .getProperty()
        );
    }

    @Test
    public void buscarConFiltros_deberiaRechazarPaginaNegativa() {

        autenticarUsuario();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.buscarConFiltros(
                        null,
                        null,
                        -1,
                        5,
                        "id",
                        "asc"
                )
        );

        verifyNoInteractions(repository);
    }

    @Test
    public void buscarConFiltros_deberiaRechazarTamanoInvalido() {

        autenticarUsuario();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.buscarConFiltros(
                        null,
                        null,
                        0,
                        101,
                        "id",
                        "asc"
                )
        );

        verifyNoInteractions(repository);
    }

    @Test
    public void buscarConFiltros_deberiaRechazarCampoOrdenacionInvalido() {

        autenticarUsuario();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.buscarConFiltros(
                        null,
                        null,
                        0,
                        5,
                        "password",
                        "asc"
                )
        );

        verifyNoInteractions(repository);
    }

    @Test
    public void buscarConFiltros_deberiaRechazarDireccionInvalida() {

        autenticarUsuario();

        assertThrows(
                IllegalArgumentException.class,
                () -> service.buscarConFiltros(
                        null,
                        null,
                        0,
                        5,
                        "id",
                        "pepito"
                )
        );

        verifyNoInteractions(repository);
    }
}