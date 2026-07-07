package com.javi.tareasAPI.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Tarea {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank(message = "El titulo no puede estar vacio")
	@Size(min = 3, max = 100, message = "El titulo debe tener entre 3 y 100 caracteres")
	private String titulo;
	
	private boolean completada;
	
	public Tarea() {
		
	}
	
	public Tarea(Integer id, String titulo, boolean completada) {
		this.id = id;
		this.titulo = titulo;
		this.completada = completada;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public boolean isCompletada() {
		return completada;
	}
	
	public void setCompletada(boolean completada) {
		this.completada = completada;
	}

	
	
}
