package app.repository;

import app.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    //Encontrar usuarios inscritos en un curso específico
    @Query("""
           SELECT u FROM Usuario u
           JOIN Inscripciones i ON i.usuario.id = u.id
           WHERE i.curso.id = :cursoId
           """)
    List<Usuario> findUsuariosPorCurso(@Param("cursoId") Long cursoId);

    //Encontrar usuarios que no están inscritos en ningún curso
    @Query("""
           SELECT u FROM Usuario u
           WHERE u.id NOT IN (SELECT i.usuario.id FROM Inscripciones i)
           """)
    List<Usuario> findUsuariosSinInscripciones();
}