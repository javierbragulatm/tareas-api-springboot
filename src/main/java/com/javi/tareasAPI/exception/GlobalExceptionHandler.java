package com.javi.tareasAPI.exception;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.http.converter.HttpMessageNotReadableException;

import com.javi.tareasAPI.dto.ErrorResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TareaNoEncontradaException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDTO manejarTareaNoEncontrada(
            TareaNoEncontradaException ex) {

        return crearErrorResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> manejarValidaciones(
            MethodArgumentNotValidException ex) {

        String mensaje = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .distinct()
                .collect(java.util.stream.Collectors.joining("; "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(crearErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        mensaje
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> manejarIllegalArgumentException(
            IllegalArgumentException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(crearErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> manejarTipoIncorrecto(
            MethodArgumentTypeMismatchException ex) {

        String mensaje = "El parámetro '" + ex.getName()
                + "' tiene un formato no válido";

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(crearErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        mensaje
                ));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDTO> manejarParametroFaltante(
            MissingServletRequestParameterException ex) {

        String mensaje = "Falta el parámetro '" + ex.getParameterName() + "'";

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(crearErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        mensaje
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> manejarJSONIncorrecto(
            HttpMessageNotReadableException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(crearErrorResponse(
                        HttpStatus.BAD_REQUEST,
                        "El cuerpo de la petición no tiene un formato válido"
                ));
    }

    private ErrorResponseDTO crearErrorResponse(
            HttpStatus status,
            String message) {

        return new ErrorResponseDTO(
                status.value(),
                status.getReasonPhrase(),
                message,
                Instant.now()
        );
    }
}