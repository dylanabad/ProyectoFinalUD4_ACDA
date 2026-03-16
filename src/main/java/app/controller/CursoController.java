package app.controller;

import lombok.RequiredArgsConstructor;
import app.model.Curso;
import app.service.CursoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "Cursos", description = "Gestión de cursos disponibles en la plataforma")
@RestController
@RequestMapping("/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    // =====================
    // CRUD
    // =====================

    @Operation(summary = "Obtener todos los cursos")
    @GetMapping
    public ResponseEntity<List<Curso>> getAll() {
        return ResponseEntity.ok(cursoService.findAll());
    }

    @Operation(summary = "Buscar curso por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Curso> getById(
            @Parameter(description = "ID del curso")
            @PathVariable Long id) {

        return ResponseEntity.ok(cursoService.findById(id));
    }

    @Operation(summary = "Crear nuevo curso")
    @PostMapping
    public ResponseEntity<Curso> create(@RequestBody Curso curso) {
        Curso creado = cursoService.save(curso);
        return ResponseEntity.status(201).body(creado);
    }

    @Operation(summary = "Actualizar un curso existente")
    @PutMapping("/{id}")
    public ResponseEntity<Curso> update(
            @Parameter(description = "ID del curso")
            @PathVariable Long id,
            @RequestBody Curso curso) {

        Curso existente = cursoService.findById(id);
        existente.setTitulo(curso.getTitulo());
        existente.setDescripcion(curso.getDescripcion());
        existente.setPlazasDisponibles(curso.getPlazasDisponibles());
        existente.setCategoria(curso.getCategoria());

        return ResponseEntity.ok(cursoService.save(existente));
    }

    @Operation(summary = "Eliminar un curso")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del curso")
            @PathVariable Long id) {

        cursoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // =====================
    // LÓGICA DE NEGOCIO
    // =====================

    @Operation(
            summary = "Inscribir usuario en un curso",
            description = "Realiza la inscripción de un usuario en un curso si hay plazas disponibles"
    )
    @PostMapping("/{cursoId}/inscribir/{usuarioId}")
    public ResponseEntity<String> inscribirUsuario(
            @Parameter(description = "ID del curso") @PathVariable Long cursoId,
            @Parameter(description = "ID del usuario") @PathVariable Long usuarioId) {

        cursoService.inscribirUsuario(cursoId, usuarioId);
        return ResponseEntity.ok("Usuario inscrito correctamente");
    }

    // =====================
    // CONSULTAS PERSONALIZADAS
    // =====================

    @Operation(summary = "Cursos con plazas disponibles")
    @GetMapping("/disponibles")
    public ResponseEntity<List<Curso>> cursosDisponibles() {
        return ResponseEntity.ok(cursoService.cursosConPlazasDisponibles());
    }

    @Operation(summary = "Cursos por categoría")
    @GetMapping("/categoria/{nombreCategoria}")
    public ResponseEntity<List<Curso>> cursosPorCategoria(
            @Parameter(description = "Nombre de la categoría")
            @PathVariable String nombreCategoria) {

        return ResponseEntity.ok(cursoService.cursosPorCategoria(nombreCategoria));
    }

    @Operation(summary = "Cursos en los que está inscrito un usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Curso>> cursosPorUsuario(
            @Parameter(description = "ID del usuario")
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(cursoService.cursosPorUsuario(usuarioId));
    }

    @Operation(summary = "Cursos con un mínimo de inscripciones")
    @GetMapping("/min-inscripciones/{cantidad}")
    public ResponseEntity<List<Curso>> cursosConMinInscripciones(
            @Parameter(description = "Número mínimo de inscripciones")
            @PathVariable long cantidad) {

        return ResponseEntity.ok(cursoService.cursosConMinInscripciones(cantidad));
    }

    @Operation(summary = "Contar estudiantes por curso")
    @GetMapping("/conteo-estudiantes")
    public ResponseEntity<List<Object[]>> contarEstudiantesPorCurso() {
        return ResponseEntity.ok(cursoService.contarEstudiantesPorCurso());
    }
}