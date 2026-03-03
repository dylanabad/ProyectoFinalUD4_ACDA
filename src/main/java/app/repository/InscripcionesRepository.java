package app.repository;

import app.model.Inscripciones;
import app.model.InscripcionesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InscripcionesRepository extends JpaRepository<Inscripciones, InscripcionesId> {

    //Obtener todas las inscripciones de un usuario
    @Query("""
           SELECT i FROM Inscripciones i
           WHERE i.usuario.id = :usuarioId
           """)
    List<Inscripciones> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    //Obtener todos los usuarios inscritos en un curso
    @Query("""
           SELECT i.usuario FROM Inscripciones i
           WHERE i.curso.id = :cursoId
           """)
    List<Object> findUsuariosByCursoId(@Param("cursoId") Long cursoId);

    //Contar inscripciones de un curso
    @Query("""
           SELECT COUNT(i) FROM Inscripciones i
           WHERE i.curso.id = :cursoId
           """)
    long contarInscripcionesPorCurso(@Param("cursoId") Long cursoId);

    //Ver cursos con plazas llenas
    @Query("""
           SELECT i.curso FROM Inscripciones i
           GROUP BY i.curso
           HAVING COUNT(i) >= i.curso.plazasDisponibles
           """)
    List<Object> findCursosLlenos();

    //Cancelar inscripciones de un usuario en todos los cursos de una categoría
    @Query("""
           DELETE FROM Inscripciones i
           WHERE i.usuario.id = :usuarioId
           AND i.curso.categoria.id = :categoriaId
           """)
    void cancelarInscripcionesPorCategoria(@Param("usuarioId") Long usuarioId,
                                           @Param("categoriaId") Long categoriaId);
}