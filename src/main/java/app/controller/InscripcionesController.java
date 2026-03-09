package app.controller;

import lombok.RequiredArgsConstructor;
import app.model.Inscripciones;
import app.model.InscripcionesId;
import app.service.InscripcionesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inscripciones")
@RequiredArgsConstructor
public class InscripcionesController {

    private final InscripcionesService inscripcionesService;

    // Obtener todas las inscripciones
    @GetMapping
    public ResponseEntity<List<Inscripciones>> getAll() {
        return ResponseEntity.ok(inscripcionesService.findAll());
    }

    // Obtener una inscripción específica
    @GetMapping("/{usuarioId}/{cursoId}")
    public ResponseEntity<Inscripciones> getById(@PathVariable Long usuarioId,
                                                 @PathVariable Long cursoId) {

        InscripcionesId id = new InscripcionesId();
        id.setUsuarioId(usuarioId);
        id.setCursoId(cursoId);

        return ResponseEntity.ok(inscripcionesService.findById(id));
    }

    // Crear inscripción manualmente
    @PostMapping
    public ResponseEntity<Inscripciones> create(@RequestBody Inscripciones inscripcion) {
        Inscripciones creada = inscripcionesService.save(inscripcion);
        return ResponseEntity.status(201).body(creada);
    }

    // Eliminar inscripción
    @DeleteMapping("/{usuarioId}/{cursoId}")
    public ResponseEntity<Void> delete(@PathVariable Long usuarioId,
                                       @PathVariable Long cursoId) {

        InscripcionesId id = new InscripcionesId();
        id.setUsuarioId(usuarioId);
        id.setCursoId(cursoId);

        inscripcionesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}