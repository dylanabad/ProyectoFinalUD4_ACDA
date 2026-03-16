package app.controller;

import lombok.RequiredArgsConstructor;
import app.model.Inscripciones;
import app.model.InscripcionesId;
import app.service.InscripcionesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "Inscripciones", description = "Gestión de inscripciones de usuarios en cursos")
@RestController
@RequestMapping("/inscripciones")
@RequiredArgsConstructor
public class InscripcionesController {

    private final InscripcionesService inscripcionesService;

    // =====================
    // CRUD
    // =====================

    @Operation(summary = "Obtener todas las inscripciones")
    @GetMapping
    public ResponseEntity<List<Inscripciones>> getAll() {
        return ResponseEntity.ok(inscripcionesService.findAll());
    }

    @Operation(summary = "Buscar inscripción por usuario y curso")
    @GetMapping("/{usuarioId}/{cursoId}")
    public ResponseEntity<Inscripciones> getById(
            @Parameter(description = "ID del usuario") @PathVariable Long usuarioId,
            @Parameter(description = "ID del curso") @PathVariable Long cursoId) {

        InscripcionesId id = new InscripcionesId();
        id.setUsuarioId(usuarioId);
        id.setCursoId(cursoId);

        return ResponseEntity.ok(inscripcionesService.findById(id));
    }

    @Operation(summary = "Crear inscripción manualmente")
    @PostMapping
    public ResponseEntity<Inscripciones> create(@RequestBody Inscripciones inscripcion) {
        Inscripciones creada = inscripcionesService.save(inscripcion);
        return ResponseEntity.status(201).body(creada);
    }

    @Operation(summary = "Eliminar inscripción")
    @DeleteMapping("/{usuarioId}/{cursoId}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del usuario") @PathVariable Long usuarioId,
            @Parameter(description = "ID del curso") @PathVariable Long cursoId) {

        InscripcionesId id = new InscripcionesId();
        id.setUsuarioId(usuarioId);
        id.setCursoId(cursoId);

        inscripcionesService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // =====================
    // CONSULTAS PERSONALIZADAS
    // =====================

    @Operation(summary = "Inscripciones de un usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Inscripciones>> inscripcionesPorUsuario(
            @Parameter(description = "ID del usuario")
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(inscripcionesService.inscripcionesPorUsuario(usuarioId));
    }

    @Operation(summary = "Usuarios inscritos en un curso")
    @GetMapping("/curso/{cursoId}/usuarios")
    public ResponseEntity<List<Object>> usuariosPorCurso(
            @Parameter(description = "ID del curso")
            @PathVariable Long cursoId) {

        return ResponseEntity.ok(inscripcionesService.usuariosPorCurso(cursoId));
    }

    @Operation(summary = "Contar inscripciones de un curso")
    @GetMapping("/curso/{cursoId}/conteo")
    public ResponseEntity<Long> contarInscripcionesCurso(
            @Parameter(description = "ID del curso")
            @PathVariable Long cursoId) {

        return ResponseEntity.ok(inscripcionesService.contarInscripcionesCurso(cursoId));
    }

    @Operation(summary = "Cursos que están llenos")
    @GetMapping("/cursos-llenos")
    public ResponseEntity<List<Object>> cursosLlenos() {
        return ResponseEntity.ok(inscripcionesService.cursosLlenos());
    }

    @Operation(summary = "Cancelar inscripciones de un usuario en cursos de una categoría")
    @DeleteMapping("/usuario/{usuarioId}/categoria/{categoriaId}")
    public ResponseEntity<Void> cancelarInscripcionesPorCategoria(
            @Parameter(description = "ID del usuario") @PathVariable Long usuarioId,
            @Parameter(description = "ID de la categoría") @PathVariable Long categoriaId) {

        inscripcionesService.cancelarInscripcionesPorCategoria(usuarioId, categoriaId);
        return ResponseEntity.noContent().build();
    }
}