package com.javi.tareasAPI.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.javi.tareasAPI.model.Tarea;
import com.javi.tareasAPI.repository.TareaRepository;

public class TareaServiceTest {

    @Mock
    private TareaRepository repository;

    @InjectMocks
    private TareaService service;

    public TareaServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void obtenerTareaExistente() {

        Tarea tarea = new Tarea(1, "Spring", false);

        when(repository.findById(1))
                .thenReturn(Optional.of(tarea));

        Tarea resultado = service.obtenerTarea(1);

        assertNotNull(resultado);

        assertEquals("Spring", resultado.getTitulo());

    }
    
    @Test
    public void crearTarea() {

        Tarea tarea = new Tarea(null, "Docker", false);

        when(repository.save(tarea))
                .thenReturn(new Tarea(1, "Docker", false));

        Tarea resultado = service.crearTarea(tarea);

        assertEquals("Docker", resultado.getTitulo());

    }
    
    @Test
    public void borrarTarea() {

        service.deleteTarea(5);

        verify(repository).deleteById(5);

    }
    
    @Test
    public void obtenerCompletadas() {

        service.obtenerCompletadas();

        verify(repository).findByCompletada(true);

    }
    
    @Test
    public void obtenerPendientes() {

        service.obtenerPendientes();

        verify(repository).findByCompletada(false);

    }

}