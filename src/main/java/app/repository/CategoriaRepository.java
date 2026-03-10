package app.repository;

import app.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    //Buscar categorías que tengan cursos activos
    @Query("""
           SELECT DISTINCT cat FROM Categoria cat
           JOIN cat.cursos c
           WHERE c.plazasDisponibles > 0
           """)
    List<Categoria> findCategoriasConCursosDisponibles();

    //Contar cuántos cursos hay en cada categoría
    @Query("""
           SELECT cat, COUNT(c) 
           FROM Categoria cat
           JOIN cat.cursos c
           GROUP BY cat
           """)
    List<Object[]> contarCursosPorCategoria();

    //Buscar categorías por nombre parcial
    @Query("""
           SELECT cat FROM Categoria cat
           WHERE LOWER(cat.nombre) LIKE LOWER(CONCAT('%', :nombreParcial, '%'))
           """)
    List<Categoria> findCategoriasPorNombre(@Param("nombreParcial") String nombreParcial);

    // Consultar categorías con más de N cursos
    @Query("""
           SELECT cat FROM Categoria cat
           JOIN cat.cursos c
           GROUP BY cat
           HAVING COUNT(c) > :minCursos
           """)
    List<Categoria> findCategoriasConMasDeNCursos(@Param("minCursos") long minCursos);

    // Obtener categorías con número de cursos asociados
    @Query("""
           SELECT c FROM Categoria c
           JOIN c.cursos cu
           WHERE cu.id = :cursoId
           """)
    List<Categoria> findCategoriasByCursoId(@Param("cursoId") Long cursoId);

}