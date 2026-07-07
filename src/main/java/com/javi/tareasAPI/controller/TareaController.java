package com.javi.tareasAPI.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import com.javi.tareasAPI.dto.TareaDTO;
import com.javi.tareasAPI.model.Tarea;
import com.javi.tareasAPI.service.TareaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(
	    name = "Tareas",
	    description = "API REST para la gestión de tareas"
	)
@RestController
@RequestMapping("/tareas")
public class TareaController {

	ArrayList<Tarea> tareas = new ArrayList<Tarea>();
	
	@Autowired
	private TareaService service;

	@Operation(
		    summary = "Obtiene todas las tareas"
		)
	@GetMapping
	public ResponseEntity<List<TareaDTO>> obtenerTareas() {

	    return ResponseEntity.ok(service.obtenerTareas());

	}
	
	@Operation(
	        summary = "Obtiene tareas paginadas"
	)
	@GetMapping("/pagina")
	public ResponseEntity<Page<TareaDTO>> obtenerTareasPaginadas(

	        @RequestParam(defaultValue = "0") int page,

	        @RequestParam(defaultValue = "5") int size) {

	    return ResponseEntity.ok(
	            service.obtenerTareasPaginadas(page, size));

	}
	
	@Operation(
	        summary = "Obtiene tareas ordenadas por título"
	)
	@GetMapping("/ordenadas")
	public ResponseEntity<Page<TareaDTO>> obtenerTareasOrdenadas(

	        @RequestParam(defaultValue = "0") int page,

	        @RequestParam(defaultValue = "5") int size) {

	    return ResponseEntity.ok(
	            service.obtenerTareasOrdenadas(page, size));

	}

	@Operation(
		    summary = "Crea una nueva tarea"
		)
	@PostMapping
	public ResponseEntity<Tarea> crearTarea(@Valid @RequestBody Tarea tarea) {

	    Tarea tareaCreada = service.crearTarea(tarea);

	    return ResponseEntity.status(201).body(tareaCreada);
	}

	@Operation(
		    summary = "Obtiene una tarea por su id"
		)
	@GetMapping("/{id}")
	public ResponseEntity<Tarea> obtenerTarea(@PathVariable Integer id) {

	    return ResponseEntity.ok(service.obtenerTarea(id));

	}

	@Operation(
		    summary = "Actualiza una tarea existente"
		)
	@PutMapping("/{id}")
	public ResponseEntity<Tarea> updateTarea(
	        @PathVariable Integer id,
	        @RequestBody Tarea tareaActualizada) {

	    return ResponseEntity.ok(service.updateTarea(id, tareaActualizada));

	}

	@Operation(
		    summary = "Elimina una tarea"
		)
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTarea(@PathVariable Integer id) {

	    service.deleteTarea(id);

	    return ResponseEntity.noContent().build();
	}
	
	@Operation(
		    summary = "Obtiene las tareas completadas"
		)
	@GetMapping("/completadas")
	public List<Tarea> obtenerCompletadas() {

		return service.obtenerCompletadas();
	}
	
	@Operation(
		    summary = "Obtiene las tareas pendientes"
		)
	@GetMapping("/pendientes")
	public List<Tarea> obtenerPendientes() {

		return service.obtenerPendientes();
	}
	
	@Operation(
		    summary = "Busca una tarea con un título específico"
		)
	@GetMapping("/buscar/{texto}")
	public List<Tarea> buscarPorTitulo(@PathVariable String texto) {

	    return service.buscarPorTitulo(texto);

	}


}