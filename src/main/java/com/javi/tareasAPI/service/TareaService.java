package com.javi.tareasAPI.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.javi.tareasAPI.repository.TareaRepository;
import com.javi.tareasAPI.dto.TareaDTO;
import com.javi.tareasAPI.exception.TareaNoEncontradaException;

import com.javi.tareasAPI.model.Tarea;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;

@Service
public class TareaService {

	@Autowired
	private TareaRepository repository;

	public List<TareaDTO> obtenerTareas() {
		List<Tarea> lista = repository.findAll();

		List<TareaDTO> listaDTO = new ArrayList<TareaDTO>();

		for (Tarea tarea : lista) {
			TareaDTO dto= new TareaDTO(tarea.getId(),tarea.getTitulo());
			listaDTO.add(dto);
		}

		return listaDTO;
	}

	public Tarea crearTarea(Tarea tarea) {

		return repository.save(tarea);
	}

	public Tarea obtenerTarea(Integer id) {

		return repository.findById(id).orElseThrow(() -> new TareaNoEncontradaException(id));
	}

	public Tarea updateTarea(Integer id, Tarea tareaActualizada) {

	    Tarea tarea = repository.findById(id)
	            .orElseThrow(() -> new TareaNoEncontradaException(id));

	    tarea.setTitulo(tareaActualizada.getTitulo());
	    tarea.setCompletada(tareaActualizada.isCompletada());

	    return repository.save(tarea);

	}
	
	public void deleteTarea(Integer id) {

		repository.deleteById(id);

	}

	public List<Tarea> obtenerCompletadas() {

		return repository.findByCompletada(true);

	}

	public List<Tarea> obtenerPendientes() {

		return repository.findByCompletada(false);
	}

	public List<Tarea> buscarPorTitulo(String texto) {

		return repository.findByTituloContaining(texto);

	}
	
	public Page<TareaDTO> obtenerTareasPaginadas(int page, int size) {

	    Pageable pageable = PageRequest.of(page, size);

	    Page<Tarea> pagina = repository.findAll(pageable);

	    return pagina.map(tarea ->
	            new TareaDTO(
	                    tarea.getId(),
	                    tarea.getTitulo()
	            ));

	}
	
	public Page<TareaDTO> obtenerTareasOrdenadas(int page, int size) {

	    Pageable pageable = PageRequest.of(
	            page,
	            size,
	            Sort.by("titulo").ascending());

	    Page<Tarea> pagina = repository.findAll(pageable);

	    return pagina.map(tarea ->
	            new TareaDTO(
	                    tarea.getId(),
	                    tarea.getTitulo()
	            ));

	}

}
