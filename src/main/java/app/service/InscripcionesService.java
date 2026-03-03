package app.service;

import lombok.RequiredArgsConstructor;
import app.model.Inscripciones;
import app.model.InscripcionesId;
import app.repository.InscripcionesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InscripcionesService {

    private final InscripcionesRepository inscripcionesRepository;

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
}