package app.repository;

import app.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    //Consulta personalizada con JOIN: cursos de un usuario
    @Query("""
           SELECT c FROM Curso c
           JOIN Inscripciones i ON i.curso.id = c.id
           WHERE i.usuario.id = :usuarioId
           """)
    List<Curso> findCursosByUsuarioId(@Param("usuarioId") Long usuarioId);

    //Buscar cursos por categoría
    @Query("""
           SELECT c FROM Curso c
           JOIN c.categoria cat
           WHERE cat.nombre = :nombreCategoria
           """)
    List<Curso> findCursosPorCategoria(@Param("nombreCategoria") String nombreCategoria);

    //Contar estudiantes inscritos en cada curso
    @Query("""
           SELECT c, COUNT(i.usuario)
           FROM Curso c
           JOIN Inscripciones i ON i.curso.id = c.id
           GROUP BY c
           """)
    List<Object[]> contarEstudiantesPorCurso();

    //Cursos con plazas disponibles
    @Query("""
           SELECT c FROM Curso c
           WHERE c.plazasDisponibles > 0
           """)
    List<Curso> findCursosConPlazasDisponibles();

    //Buscar cursos que superen un número mínimo de inscripciones
    @Query("""
           SELECT c FROM Curso c
           JOIN Inscripciones i ON i.curso.id = c.id
           GROUP BY c
           HAVING COUNT(i.usuario) >= :minInscripciones
           """)
    List<Curso> findCursosConMinInscripciones(@Param("minInscripciones") long minInscripciones);

    //Actualizar plazas disponibles al inscribir un usuario (operación avanzada)
    @Query("""
           UPDATE Curso c SET c.plazasDisponibles = c.plazasDisponibles - 1
           WHERE c.id = :cursoId AND c.plazasDisponibles > 0
           """)
    int decrementarPlazas(@Param("cursoId") Long cursoId);

}