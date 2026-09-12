package com.javi.tareasAPI.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.javi.tareasAPI.model.Tarea;
import com.javi.tareasAPI.model.Usuario;
import com.javi.tareasAPI.repository.TareaRepository;
import com.javi.tareasAPI.repository.UsuarioRepository;

import com.javi.tareasAPI.exception.TareaNoEncontradaException;

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

    @Test
    public void obtenerTareaExistente() {

        autenticarUsuario();

        Tarea tarea = new Tarea(1, "Spring", false);

        when(repository.findByIdAndUsuarioUsername(1, "javi"))
                .thenReturn(Optional.of(tarea));

        Tarea resultado = service.obtenerTarea(1);

        assertNotNull(resultado);
        assertEquals("Spring", resultado.getTitulo());

    }

    @Test
    public void crearTarea() {

        autenticarUsuario();

        Tarea tarea = new Tarea(null, "Docker", false);

        Usuario usuario = new Usuario();

        when(usuarioRepository.findByUsername("javi"))
                .thenReturn(Optional.of(usuario));

        when(repository.save(tarea))
                .thenReturn(new Tarea(1, "Docker", false));

        Tarea resultado = service.crearTarea(tarea);

        assertEquals("Docker", resultado.getTitulo());

        verify(usuarioRepository).findByUsername("javi");
        verify(repository).save(tarea);

    }
    
    @Test
    public void crearTarea_deberiaAsignarUsuarioActual() {

        autenticarUsuario();

        Tarea tarea = new Tarea(null, "Docker", false);

        Usuario usuario = new Usuario();
        usuario.setUsername("javi");

        when(usuarioRepository.findByUsername("javi"))
                .thenReturn(Optional.of(usuario));

        when(repository.save(tarea))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Tarea resultado = service.crearTarea(tarea);

        assertNotNull(resultado);
        assertEquals("Docker", resultado.getTitulo());

        assertEquals(
                usuario,
                resultado.getUsuario());

        verify(usuarioRepository).findByUsername("javi");
        verify(repository).save(tarea);
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
                () -> service.deleteTarea(5));

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
                .thenReturn(java.util.List.of(tarea1, tarea2));

        service.obtenerTareas();

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
                () -> service.obtenerTarea(5));
    }
    
    @Test
    public void updateTarea_deberiaRechazarTareaDeOtroUsuario() {

        autenticarUsuario();

        Tarea tareaActualizada =
                new Tarea(null, "Nuevo titulo", true);

        when(repository.findByIdAndUsuarioUsername(5, "javi"))
                .thenReturn(Optional.empty());

        assertThrows(
                TareaNoEncontradaException.class,
                () -> service.updateTarea(
                        5,
                        tareaActualizada));

        verify(repository, never())
                .save(any(Tarea.class));
    }

}