package com.javi.tareasAPI.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.javi.tareasAPI.model.Tarea;

public interface TareaRepository extends JpaRepository<Tarea, Integer> {

	List<Tarea> findByUsuarioUsername(String username);

	Optional<Tarea> findByIdAndUsuarioUsername(Integer id, String username);

	Page<Tarea> findByUsuarioUsername(String username, Pageable pageable);

	List<Tarea> findByUsuarioUsernameAndCompletada(String username, boolean completada);

	List<Tarea> findByUsuarioUsernameAndTituloContaining(String username, String texto);

	@Query("""
			SELECT t
			FROM Tarea t
			WHERE t.usuario.username = :username
			AND (:texto IS NULL OR LOWER(t.titulo) LIKE LOWER(CONCAT('%', :texto, '%')))
			AND (:completada IS NULL OR t.completada = :completada)
			""")
	Page<Tarea> buscarConFiltros(@Param("username") String username, @Param("texto") String texto,
			@Param("completada") Boolean completada, Pageable pageable);

}
