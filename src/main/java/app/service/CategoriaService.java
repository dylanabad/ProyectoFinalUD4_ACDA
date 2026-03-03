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

    // CRUD básico
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

    //Obtener categorías de un curso específico
    public List<Categoria> categoriasPorCurso(Long cursoId) {
        return categoriaRepository.findCategoriasByCursoId(cursoId);
    }

    //Actualizar nombre de categoría en todos los cursos relacionados
    public void actualizarNombreCategoriaEnCursos(Long categoriaId, String nuevoNombre) {
        Categoria categoria = findById(categoriaId);
        categoria.setNombre(nuevoNombre);
        categoriaRepository.save(categoria);
    }
}