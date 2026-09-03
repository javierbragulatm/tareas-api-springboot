package controller;

import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.eq;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.javi.tareasAPI.controller.AuthController;
import com.javi.tareasAPI.dto.LoginRequest;
import com.javi.tareasAPI.dto.RegisterRequest;
import com.javi.tareasAPI.model.Usuario;
import com.javi.tareasAPI.security.JwtService;
import com.javi.tareasAPI.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private AuthController authController;

    @Test
    void login_deberiaDevolverTokenConCredencialesCorrectas() {

        LoginRequest request = new LoginRequest();

        request.setUsername("javi");
        request.setPassword("1234");

        Usuario usuario = new Usuario();

        usuario.setUsername("javi");
        usuario.setPassword(
                new BCryptPasswordEncoder()
                        .encode("1234"));

        when(usuarioService.buscarPorUsername("javi"))
                .thenReturn(usuario);

        when(usuarioService.comprobarPassword(
                "1234",
                usuario.getPassword()))
                .thenReturn(true);

        when(jwtService.generarToken("javi"))
                .thenReturn("token-de-prueba");

        String resultado =
                authController.login(request);

        assertEquals(
                "token-de-prueba",
                resultado);
    }

    @Test
    void register_deberiaRegistrarUsuario() {

        RegisterRequest request =
                new RegisterRequest();

        request.setUsername("javi");
        request.setPassword("1234");

        authController.register(request);

        verify(usuarioService).registrarUsuario(
                eq("javi"),
                eq("1234"));
    }
}