package com.javi.tareasAPI.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.javi.tareasAPI.dto.LoginRequest;
import com.javi.tareasAPI.security.JwtService;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        if (request.getUsername().equals("javi")
                && request.getPassword().equals("1234")) {

            return jwtService.generarToken(request.getUsername());

        }

        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Credenciales incorrectas");

    }

}