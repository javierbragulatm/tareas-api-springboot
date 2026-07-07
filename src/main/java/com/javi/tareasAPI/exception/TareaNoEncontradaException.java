package com.javi.tareasAPI.exception;


public class TareaNoEncontradaException extends RuntimeException {

   
	private static final long serialVersionUID = 1L;

	public TareaNoEncontradaException(Integer id) {
        super("No existe la tarea con id " + id);
    }

}