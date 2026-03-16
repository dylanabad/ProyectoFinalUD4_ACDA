package app.controller;

import lombok.RequiredArgsConstructor;
import app.model.Curso;
import app.service.CursoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@Tag(name = "Cursos", description = "Gestión de cursos disponibles en la plataforma")
@RestController
@RequestMapping("/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    // =====================
    // CRUD
    // =====================

    @Operation(
            summary = "Obtener todos los cursos",
            description = "Devuelve todos los cursos registrados en la plataforma",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping
    public ResponseEntity<List<Curso>> getAll() {
        return ResponseEntity.ok(cursoService.findAll());
    }

    @Operation(
            summary = "Buscar curso por ID",
            description = "Devuelve un curso según su ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Curso encontrado correctamente"),
                    @ApiResponse(responseCode = "404", description = "Curso no encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Curso> getById(
            @Parameter(description = "ID del curso", example = "1")
            @PathVariable Long id) {

        return ResponseEntity.ok(cursoService.findById(id));
    }

    @Operation(
            summary = "Crear nuevo curso",
            description = "Crea un nuevo curso en la plataforma",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Curso creado correctamente"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
            }
    )
    @PostMapping
    public ResponseEntity<Curso> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto JSON con los datos del curso",
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Ejemplo de curso",
                                    value = "{ \"titulo\": \"Java Avanzado\", \"descripcion\": \"Curso completo de Java\", \"plazasDisponibles\": 20, \"categoria\": { \"id\": 1 } }"
                            )
                    )
            )
            @RequestBody Curso curso) {

        Curso creado = cursoService.save(curso);
        return ResponseEntity.status(201).body(creado);
    }

    @Operation(
            summary = "Actualizar un curso existente",
            description = "Actualiza los datos de un curso según su ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Curso actualizado correctamente"),
                    @ApiResponse(responseCode = "404", description = "Curso no encontrado"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Curso> update(
            @Parameter(description = "ID del curso a actualizar", example = "1")
            @PathVariable Long id,
            @RequestBody Curso curso) {

        Curso existente = cursoService.findById(id);
        existente.setTitulo(curso.getTitulo());
        existente.setDescripcion(curso.getDescripcion());
        existente.setPlazasDisponibles(curso.getPlazasDisponibles());
        existente.setCategoria(curso.getCategoria());

        return ResponseEntity.ok(cursoService.save(existente));
    }

    @Operation(
            summary = "Eliminar un curso",
            description = "Elimina un curso según su ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Curso eliminado correctamente"),
                    @ApiResponse(responseCode = "404", description = "Curso no encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del curso a eliminar", example = "1")
            @PathVariable Long id) {

        cursoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // =====================
    // LÓGICA DE NEGOCIO
    // =====================

    @Operation(
            summary = "Inscribir usuario en un curso",
            description = "Realiza la inscripción de un usuario en un curso si hay plazas disponibles",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario inscrito correctamente"),
                    @ApiResponse(responseCode = "400", description = "No quedan plazas disponibles o datos inválidos"),
                    @ApiResponse(responseCode = "404", description = "Curso o usuario no encontrado")
            }
    )
    @PostMapping("/{cursoId}/inscribir/{usuarioId}")
    public ResponseEntity<String> inscribirUsuario(
            @Parameter(description = "ID del curso", example = "1") @PathVariable Long cursoId,
            @Parameter(description = "ID del usuario", example = "2") @PathVariable Long usuarioId) {

        cursoService.inscribirUsuario(cursoId, usuarioId);
        return ResponseEntity.ok("Usuario inscrito correctamente");
    }

    // =====================
    // CONSULTAS PERSONALIZADAS
    // =====================

    @Operation(
            summary = "Cursos con plazas disponibles",
            description = "Devuelve los cursos que aún tienen plazas disponibles",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
            }
    )
    @GetMapping("/disponibles")
    public ResponseEntity<List<Curso>> cursosDisponibles() {
        return ResponseEntity.ok(cursoService.cursosConPlazasDisponibles());
    }

    @Operation(
            summary = "Cursos por categoría",
            description = "Devuelve los cursos asociados a una categoría específica",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
                    @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
            }
    )
    @GetMapping("/categoria/{nombreCategoria}")
    public ResponseEntity<List<Curso>> cursosPorCategoria(
            @Parameter(description = "Nombre de la categoría", example = "Programación")
            @PathVariable String nombreCategoria) {

        return ResponseEntity.ok(cursoService.cursosPorCategoria(nombreCategoria));
    }

    @Operation(
            summary = "Cursos en los que está inscrito un usuario",
            description = "Devuelve todos los cursos en los que un usuario específico está inscrito",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            }
    )
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Curso>> cursosPorUsuario(
            @Parameter(description = "ID del usuario", example = "2")
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(cursoService.cursosPorUsuario(usuarioId));
    }

    @Operation(
            summary = "Cursos con un mínimo de inscripciones",
            description = "Devuelve cursos que tienen al menos la cantidad mínima de inscripciones indicada",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
            }
    )
    @GetMapping("/min-inscripciones/{cantidad}")
    public ResponseEntity<List<Curso>> cursosConMinInscripciones(
            @Parameter(description = "Número mínimo de inscripciones", example = "5")
            @PathVariable long cantidad) {

        return ResponseEntity.ok(cursoService.cursosConMinInscripciones(cantidad));
    }

    @Operation(
            summary = "Contar estudiantes por curso",
            description = "Devuelve cada curso junto con el número de estudiantes inscritos. Cada Object[] contiene: [0]=Curso, [1]=Número de estudiantes",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
            }
    )
    @GetMapping("/conteo-estudiantes")
    public ResponseEntity<List<Object[]>> contarEstudiantesPorCurso() {
        return ResponseEntity.ok(cursoService.contarEstudiantesPorCurso());
    }
}