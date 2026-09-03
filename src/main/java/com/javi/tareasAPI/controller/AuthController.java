package com.javi.tareasAPI.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.javi.tareasAPI.dto.LoginRequest;
import com.javi.tareasAPI.dto.RegisterRequest;
import com.javi.tareasAPI.model.Usuario;
import com.javi.tareasAPI.security.JwtService;
import com.javi.tareasAPI.service.UsuarioService;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@RestController
public class AuthController {

    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    public AuthController(
            JwtService jwtService,
            UsuarioService usuarioService) {

        this.jwtService = jwtService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest request) {

        usuarioService.registrarUsuario(
                request.getUsername(),
                request.getPassword());
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest request) {

        Usuario usuario;

        try {
            usuario = usuarioService.buscarPorUsername(
                    request.getUsername());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Credenciales incorrectas");
        }

        if (!usuarioService.comprobarPassword(
                request.getPassword(),
                usuario.getPassword())) {

            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Credenciales incorrectas");
        }

        return jwtService.generarToken(usuario.getUsername());
    }
}