package com.javi.tareasAPI.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.javi.tareasAPI.model.Usuario;
import com.javi.tareasAPI.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrarUsuario(String username, String password) {

        if (usuarioRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("El usuario ya existe");
        }

        Usuario usuario = new Usuario();

        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));

        return usuarioRepository.save(usuario);
    }

    public Usuario buscarPorUsername(String username) {

        return usuarioRepository.findByUsername(username)
                .orElseThrow(() ->
                    new IllegalArgumentException("Usuario no encontrado"));
    }

    public boolean comprobarPassword(
            String password,
            String passwordHash) {

        return passwordEncoder.matches(password, passwordHash);
    }
}