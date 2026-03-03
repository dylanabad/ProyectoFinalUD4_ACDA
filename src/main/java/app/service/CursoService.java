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

    // CRUD básico
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

    //Funcionalidad avanzada: inscribir usuario a un curso
    @Transactional
    public void inscribirUsuario(Long cursoId, Long usuarioId) {
        Curso curso = findById(cursoId);
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (curso.getPlazasDisponibles() <= 0) {
            throw new RuntimeException("No quedan plazas disponibles");
        }

        // Crear inscripción
        Inscripciones inscripcion = new Inscripciones();
        inscripcion.setCurso(curso);
        inscripcion.setUsuario(usuario);
        inscripcion.setFechaInscripcion(LocalDate.now());
        InscripcionesId id = new InscripcionesId();
        id.setCursoId(curso.getId());
        id.setUsuarioId(usuario.getId());
        inscripcion.setId(id);

        // Añadir inscripción a listas de referencia (opcional, útil para bi-direccional)
        curso.getInscripciones().add(inscripcion);
        usuario.getInscripciones().add(inscripcion);

        // Reducir plazas
        curso.setPlazasDisponibles(curso.getPlazasDisponibles() - 1);

        // Guardar cambios
        cursoRepository.save(curso);
    }
}