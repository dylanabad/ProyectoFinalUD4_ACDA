package app.service;

import lombok.RequiredArgsConstructor;
import app.model.Inscripciones;
import app.model.InscripcionesId;
import app.repository.InscripcionesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InscripcionesService {

    private final InscripcionesRepository inscripcionesRepository;

    // =====================
    // CRUD BÁSICO
    // =====================

    public List<Inscripciones> findAll() {
        return inscripcionesRepository.findAll();
    }

    public Inscripciones findById(InscripcionesId id) {
        return inscripcionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));
    }

    public Inscripciones save(Inscripciones inscripcion) {
        return inscripcionesRepository.save(inscripcion);
    }

    public void delete(InscripcionesId id) {
        inscripcionesRepository.deleteById(id);
    }

    // =====================
    // CONSULTAS PERSONALIZADAS
    // =====================

    // Obtener todas las inscripciones de un usuario
    public List<Inscripciones> inscripcionesPorUsuario(Long usuarioId) {
        return inscripcionesRepository.findByUsuarioId(usuarioId);
    }

    // Obtener usuarios inscritos en un curso
    public List<Object> usuariosPorCurso(Long cursoId) {
        return inscripcionesRepository.findUsuariosByCursoId(cursoId);
    }

    // Contar inscripciones de un curso
    public long contarInscripcionesCurso(Long cursoId) {
        return inscripcionesRepository.contarInscripcionesPorCurso(cursoId);
    }

    // Obtener cursos que están llenos
    public List<Object> cursosLlenos() {
        return inscripcionesRepository.findCursosLlenos();
    }

    // Cancelar inscripciones de un usuario en todos los cursos de una categoría
    @Transactional
    public void cancelarInscripcionesPorCategoria(Long usuarioId, Long categoriaId) {
        inscripcionesRepository.cancelarInscripcionesPorCategoria(usuarioId, categoriaId);
    }
}