package com.javi.tareasAPI.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.javi.tareasAPI.model.Usuario;
import com.javi.tareasAPI.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private PasswordEncoder passwordEncoder;

    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();

        usuarioService = new UsuarioService(
                usuarioRepository,
                passwordEncoder);
    }

    @Test
    void registrarUsuario_deberiaGuardarUsuarioConPasswordEncriptada() {

        when(usuarioRepository.existsByUsername("javi"))
                .thenReturn(false);

        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Usuario usuario = usuarioService.registrarUsuario(
                "javi",
                "1234");

        assertEquals("javi", usuario.getUsername());

        assertTrue(
                passwordEncoder.matches(
                        "1234",
                        usuario.getPassword()));

        assertFalse(
                usuario.getPassword().equals("1234"));

        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void registrarUsuario_deberiaRechazarUsuarioExistente() {

        when(usuarioRepository.existsByUsername("javi"))
                .thenReturn(true);

        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> usuarioService.registrarUsuario(
                                "javi",
                                "1234"));

        assertEquals(
                "El usuario ya existe",
                exception.getMessage());
    }

    @Test
    void comprobarPassword_deberiaDevolverTrueConPasswordCorrecta() {

        String password = "1234";

        String passwordHash =
                passwordEncoder.encode(password);

        boolean resultado =
                usuarioService.comprobarPassword(
                        password,
                        passwordHash);

        assertTrue(resultado);
    }

    @Test
    void comprobarPassword_deberiaDevolverFalseConPasswordIncorrecta() {

        String passwordHash =
                passwordEncoder.encode("1234");

        boolean resultado =
                usuarioService.comprobarPassword(
                        "incorrecta",
                        passwordHash);

        assertFalse(resultado);
    }

    @Test
    void buscarPorUsername_deberiaDevolverUsuarioExistente() {

        Usuario usuario = new Usuario();
        usuario.setUsername("javi");
        usuario.setPassword("hash");

        when(usuarioRepository.findByUsername("javi"))
                .thenReturn(Optional.of(usuario));

        Usuario resultado =
                usuarioService.buscarPorUsername("javi");

        assertEquals("javi", resultado.getUsername());
    }

    @Test
    void buscarPorUsername_deberiaLanzarExcepcionSiNoExiste() {

        when(usuarioRepository.findByUsername("javi"))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> usuarioService.buscarPorUsername("javi"));
    }
}