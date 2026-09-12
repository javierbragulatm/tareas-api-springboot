package com.javi.tareasAPI.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.javi.tareasAPI.model.Tarea;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TareaRepository extends JpaRepository<Tarea, Integer> {
	
	List<Tarea> findByUsuarioUsername(String username);

	Optional<Tarea> findByIdAndUsuarioUsername(
	        Integer id,
	        String username);

	Page<Tarea> findByUsuarioUsername(
	        String username,
	        Pageable pageable);
	
	List<Tarea> findByUsuarioUsernameAndCompletada(
	        String username,
	        boolean completada);

	List<Tarea> findByUsuarioUsernameAndTituloContaining(
	        String username,
	        String texto);

}