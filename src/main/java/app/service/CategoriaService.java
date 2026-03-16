package app.service;

import lombok.RequiredArgsConstructor;
import app.model.Categoria;
import app.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    // =====================
    // CRUD BÁSICO
    // =====================

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Categoria findById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    public Categoria save(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public void delete(Long id) {
        categoriaRepository.deleteById(id);
    }

    // =====================
    // CONSULTAS PERSONALIZADAS
    // =====================

    // Obtener categorías de un curso específico
    public List<Categoria> categoriasPorCurso(Long cursoId) {
        return categoriaRepository.findCategoriasByCursoId(cursoId);
    }

    // Categorías que tienen cursos con plazas disponibles
    public List<Categoria> categoriasConCursosDisponibles() {
        return categoriaRepository.findCategoriasConCursosDisponibles();
    }

    // Buscar categorías por nombre parcial
    public List<Categoria> buscarCategoriasPorNombre(String nombreParcial) {
        return categoriaRepository.findCategoriasPorNombre(nombreParcial);
    }

    // Categorías con más de N cursos
    public List<Categoria> categoriasConMasDeNCursos(long minCursos) {
        return categoriaRepository.findCategoriasConMasDeNCursos(minCursos);
    }

    // Contar cuántos cursos hay en cada categoría
    public List<Object[]> contarCursosPorCategoria() {
        return categoriaRepository.contarCursosPorCategoria();
    }

    // =====================
    // LÓGICA DE NEGOCIO
    // =====================

    // Actualizar nombre de categoría
    public void actualizarNombreCategoriaEnCursos(Long categoriaId, String nuevoNombre) {
        Categoria categoria = findById(categoriaId);
        categoria.setNombre(nuevoNombre);
        categoriaRepository.save(categoria);
    }
}