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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@Tag(name = "Inscripciones", description = "Gestión de inscripciones de usuarios en cursos")
@RestController
@RequestMapping("/inscripciones")
@RequiredArgsConstructor
public class InscripcionesController {

    private final InscripcionesService inscripcionesService;

    // =====================
    // CRUD
    // =====================

    @Operation(
            summary = "Obtener todas las inscripciones",
            description = "Devuelve todas las inscripciones de usuarios en cursos",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping
    public ResponseEntity<List<Inscripciones>> getAll() {
        return ResponseEntity.ok(inscripcionesService.findAll());
    }

    @Operation(
            summary = "Buscar inscripción por usuario y curso",
            description = "Devuelve la inscripción de un usuario específico en un curso específico",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Inscripción encontrada"),
                    @ApiResponse(responseCode = "404", description = "Inscripción no encontrada")
            }
    )
    @GetMapping("/{usuarioId}/{cursoId}")
    public ResponseEntity<Inscripciones> getById(
            @Parameter(description = "ID del usuario", example = "2") @PathVariable Long usuarioId,
            @Parameter(description = "ID del curso", example = "1") @PathVariable Long cursoId) {

        InscripcionesId id = new InscripcionesId();
        id.setUsuarioId(usuarioId);
        id.setCursoId(cursoId);

        return ResponseEntity.ok(inscripcionesService.findById(id));
    }

    @Operation(
            summary = "Crear inscripción manualmente",
            description = "Crea una inscripción manual de un usuario en un curso",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Inscripción creada correctamente"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
            }
    )
    @PostMapping
    public ResponseEntity<Inscripciones> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto JSON con los datos de la inscripción",
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Ejemplo inscripción",
                                    value = "{ \"id\": { \"usuarioId\": 2, \"cursoId\": 1 }, \"fechaInscripcion\": \"2026-03-16\" }"
                            )
                    )
            )
            @RequestBody Inscripciones inscripcion) {

        Inscripciones creada = inscripcionesService.save(inscripcion);
        return ResponseEntity.status(201).body(creada);
    }

    @Operation(
            summary = "Eliminar inscripción",
            description = "Elimina la inscripción de un usuario en un curso",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Inscripción eliminada correctamente"),
                    @ApiResponse(responseCode = "404", description = "Inscripción no encontrada")
            }
    )
    @DeleteMapping("/{usuarioId}/{cursoId}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del usuario", example = "2") @PathVariable Long usuarioId,
            @Parameter(description = "ID del curso", example = "1") @PathVariable Long cursoId) {

        InscripcionesId id = new InscripcionesId();
        id.setUsuarioId(usuarioId);
        id.setCursoId(cursoId);

        inscripcionesService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // =====================
    // CONSULTAS PERSONALIZADAS
    // =====================

    @Operation(
            summary = "Inscripciones de un usuario",
            description = "Devuelve todas las inscripciones de un usuario específico",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            }
    )
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Inscripciones>> inscripcionesPorUsuario(
            @Parameter(description = "ID del usuario", example = "2")
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(inscripcionesService.inscripcionesPorUsuario(usuarioId));
    }

    @Operation(
            summary = "Usuarios inscritos en un curso",
            description = "Devuelve todos los usuarios inscritos en un curso específico",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
                    @ApiResponse(responseCode = "404", description = "Curso no encontrado")
            }
    )
    @GetMapping("/curso/{cursoId}/usuarios")
    public ResponseEntity<List<Object>> usuariosPorCurso(
            @Parameter(description = "ID del curso", example = "1")
            @PathVariable Long cursoId) {

        return ResponseEntity.ok(inscripcionesService.usuariosPorCurso(cursoId));
    }

    @Operation(
            summary = "Contar inscripciones de un curso",
            description = "Devuelve el número de inscripciones de un curso específico",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Número de inscripciones obtenido correctamente")
            }
    )
    @GetMapping("/curso/{cursoId}/conteo")
    public ResponseEntity<Long> contarInscripcionesCurso(
            @Parameter(description = "ID del curso", example = "1")
            @PathVariable Long cursoId) {

        return ResponseEntity.ok(inscripcionesService.contarInscripcionesCurso(cursoId));
    }

    @Operation(
            summary = "Cursos que están llenos",
            description = "Devuelve todos los cursos que no tienen plazas disponibles",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
            }
    )
    @GetMapping("/cursos-llenos")
    public ResponseEntity<List<Object>> cursosLlenos() {
        return ResponseEntity.ok(inscripcionesService.cursosLlenos());
    }

    @Operation(
            summary = "Cancelar inscripciones de un usuario en cursos de una categoría",
            description = "Elimina todas las inscripciones de un usuario en todos los cursos de la categoría indicada",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Inscripciones canceladas correctamente"),
                    @ApiResponse(responseCode = "404", description = "Usuario o categoría no encontrados")
            }
    )
    @DeleteMapping("/usuario/{usuarioId}/categoria/{categoriaId}")
    public ResponseEntity<Void> cancelarInscripcionesPorCategoria(
            @Parameter(description = "ID del usuario", example = "2") @PathVariable Long usuarioId,
            @Parameter(description = "ID de la categoría", example = "1") @PathVariable Long categoriaId) {

        inscripcionesService.cancelarInscripcionesPorCategoria(usuarioId, categoriaId);
        return ResponseEntity.noContent().build();
    }
}