package repository;

import model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    // Consulta personalizada obligatoria con JOIN
    @Query("""
           SELECT c FROM Curso c
           JOIN Inscripciones i ON i.curso.id = c.id
           WHERE i.usuario.id = :usuarioId
           """)
    List<Curso> findCursosByUsuarioId(@Param("usuarioId") Long usuarioId);
}