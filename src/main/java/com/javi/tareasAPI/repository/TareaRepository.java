package com.javi.tareasAPI.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javi.tareasAPI.model.Tarea;

public interface TareaRepository extends JpaRepository<Tarea, Integer> {
	
	List<Tarea> findByCompletada(boolean completada);
	List<Tarea> findByTituloContaining(String texto);

}