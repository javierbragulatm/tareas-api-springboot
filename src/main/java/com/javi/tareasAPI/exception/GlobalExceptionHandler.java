package com.javi.tareasAPI.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TareaNoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> manejarTareaNoEncontrada(
            TareaNoEncontradaException ex) {

        Map<String, String> error = new HashMap<>();

        error.put("mensaje", ex.getMessage());

        return error;
    }
    
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    @ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST)
    public Map<String, String> manejarValidaciones(
            org.springframework.web.bind.MethodArgumentNotValidException ex) {

        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errores.put(error.getField(), error.getDefaultMessage());
        });

        return errores;
    }
    
}