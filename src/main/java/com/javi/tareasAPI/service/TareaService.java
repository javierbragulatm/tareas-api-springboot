package com.javi.tareasAPI.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.javi.tareasAPI.repository.TareaRepository;
import com.javi.tareasAPI.dto.TareaDTO;
import com.javi.tareasAPI.exception.TareaNoEncontradaException;

import com.javi.tareasAPI.model.Tarea;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;

import org.springframework.security.core.context.SecurityContextHolder;
import com.javi.tareasAPI.model.Usuario;
import com.javi.tareasAPI.repository.UsuarioRepository;

@Service
public class TareaService {

	private final TareaRepository repository;
	private final UsuarioRepository usuarioRepository;

	public TareaService(TareaRepository repository, UsuarioRepository usuarioRepository) {

		this.repository = repository;
		this.usuarioRepository = usuarioRepository;
	}

	public List<TareaDTO> obtenerTareas() {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		List<Tarea> lista = repository.findByUsuarioUsername(username);

		List<TareaDTO> listaDTO = new ArrayList<>();

		for (Tarea tarea : lista) {

			TareaDTO dto = new TareaDTO(tarea.getId(), tarea.getTitulo());

			listaDTO.add(dto);
		}

		return listaDTO;
	}

	public Tarea crearTarea(Tarea tarea) {

		Usuario usuario = obtenerUsuarioActual();

		tarea.setUsuario(usuario);

		return repository.save(tarea);
	}

	public Tarea obtenerTarea(Integer id) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		return repository.findByIdAndUsuarioUsername(id, username)
				.orElseThrow(() -> new TareaNoEncontradaException(id));
	}

	public Tarea updateTarea(Integer id, Tarea tareaActualizada) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		Tarea tarea = repository.findByIdAndUsuarioUsername(id, username)
				.orElseThrow(() -> new TareaNoEncontradaException(id));

		tarea.setTitulo(tareaActualizada.getTitulo());

		tarea.setCompletada(tareaActualizada.isCompletada());

		return repository.save(tarea);
	}

	public void deleteTarea(Integer id) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		Tarea tarea = repository.findByIdAndUsuarioUsername(id, username)
				.orElseThrow(() -> new TareaNoEncontradaException(id));

		repository.delete(tarea);
	}

	public List<Tarea> obtenerCompletadas() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		return repository.findByUsuarioUsernameAndCompletada(username, true);
	}

	public List<Tarea> obtenerPendientes() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		return repository.findByUsuarioUsernameAndCompletada(username, false);
	}

	public List<Tarea> buscarPorTitulo(String texto) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		return repository.findByUsuarioUsernameAndTituloContaining(username, texto);
	}

	public Page<TareaDTO> obtenerTareasPaginadas(int page, int size) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		Pageable pageable = PageRequest.of(page, size);

		Page<Tarea> pagina = repository.findByUsuarioUsername(username, pageable);

		return pagina.map(tarea -> new TareaDTO(tarea.getId(), tarea.getTitulo()));

	}

	public Page<TareaDTO> obtenerTareasOrdenadas(int page, int size) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		Pageable pageable = PageRequest.of(page, size, Sort.by("titulo").ascending());

		Page<Tarea> pagina = repository.findByUsuarioUsername(username, pageable);

		return pagina.map(tarea -> new TareaDTO(tarea.getId(), tarea.getTitulo()));

	}

	public Page<TareaDTO> buscarConFiltros(String texto, Boolean completada, int page, int size, String sort,
			String direction) {

		if (page < 0) {
			throw new IllegalArgumentException("El número de página no puede ser negativo");
		}

		if (size < 1 || size > 100) {
			throw new IllegalArgumentException("El tamaño de página debe estar entre 1 y 100");
		}

		if (!sort.equals("id") && !sort.equals("titulo") && !sort.equals("completada")) {

			throw new IllegalArgumentException("Campo de ordenación no válido");
		}

		if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {

			throw new IllegalArgumentException("La dirección debe ser 'asc' o 'desc'");
		}

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

		Page<Tarea> pagina = repository.buscarConFiltros(username, texto, completada, pageable);

		return pagina.map(tarea -> new TareaDTO(tarea.getId(), tarea.getTitulo()));
	}

	private Usuario obtenerUsuarioActual() {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		return usuarioRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
	}

}
