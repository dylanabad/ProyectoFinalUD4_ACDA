package app.service;

import lombok.RequiredArgsConstructor;
import app.model.Curso;
import app.model.Usuario;
import app.model.Inscripciones;
import app.model.InscripcionesId;
import app.repository.CursoRepository;
import app.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;

    // =====================
    // CRUD BÁSICO
    // =====================

    public List<Curso> findAll() {
        return cursoRepository.findAll();
    }

    public Curso findById(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
    }

    public Curso save(Curso curso) {
        return cursoRepository.save(curso);
    }

    public void delete(Long id) {
        cursoRepository.deleteById(id);
    }

    // =====================
    // CONSULTAS PERSONALIZADAS
    // =====================

    // Cursos en los que está inscrito un usuario
    public List<Curso> cursosPorUsuario(Long usuarioId) {
        return cursoRepository.findCursosByUsuarioId(usuarioId);
    }

    // Cursos de una categoría concreta
    public List<Curso> cursosPorCategoria(String nombreCategoria) {
        return cursoRepository.findCursosPorCategoria(nombreCategoria);
    }

    // Cursos con plazas disponibles
    public List<Curso> cursosConPlazasDisponibles() {
        return cursoRepository.findCursosConPlazasDisponibles();
    }

    // Cursos con un mínimo de inscripciones
    public List<Curso> cursosConMinInscripciones(long minInscripciones) {
        return cursoRepository.findCursosConMinInscripciones(minInscripciones);
    }

    // Contar estudiantes inscritos en cada curso
    public List<Object[]> contarEstudiantesPorCurso() {
        return cursoRepository.contarEstudiantesPorCurso();
    }

    // =====================
    // LÓGICA DE NEGOCIO
    // =====================

    // Inscribir usuario a un curso
    @Transactional
    public void inscribirUsuario(Long cursoId, Long usuarioId) {

        // Usamos la query UPDATE del repository
        int plazasActualizadas = cursoRepository.decrementarPlazas(cursoId);

        if (plazasActualizadas == 0) {
            throw new RuntimeException("No quedan plazas disponibles");
        }

        Curso curso = findById(cursoId);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear inscripción
        Inscripciones inscripcion = new Inscripciones();
        inscripcion.setCurso(curso);
        inscripcion.setUsuario(usuario);
        inscripcion.setFechaInscripcion(LocalDate.now());

        InscripcionesId id = new InscripcionesId();
        id.setCursoId(curso.getId());
        id.setUsuarioId(usuario.getId());
        inscripcion.setId(id);

        // Añadir inscripción a listas
        curso.getInscripciones().add(inscripcion);
        usuario.getInscripciones().add(inscripcion);

        // Guardar
        cursoRepository.save(curso);
    }
}