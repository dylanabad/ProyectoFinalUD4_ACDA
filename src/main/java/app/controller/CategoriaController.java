package app.controller;

import lombok.RequiredArgsConstructor;
import app.model.Categoria;
import app.service.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@Tag(name = "Categorías", description = "Gestión de categorías de cursos")
@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    // =====================
    // CRUD
    // =====================

    @Operation(
            summary = "Obtener todas las categorías",
            description = "Devuelve una lista con todas las categorías disponibles en la plataforma",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida correctamente"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping
    public ResponseEntity<List<Categoria>> getAll() {
        return ResponseEntity.ok(categoriaService.findAll());
    }

    @Operation(
            summary = "Buscar categoría por ID",
            description = "Devuelve una categoría específica según su ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categoría encontrada correctamente"),
                    @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getById(
            @Parameter(description = "ID de la categoría", example = "1")
            @PathVariable Long id) {

        return ResponseEntity.ok(categoriaService.findById(id));
    }

    @Operation(
            summary = "Crear nueva categoría",
            description = "Crea una nueva categoría en la plataforma",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Categoría creada correctamente"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
            }
    )
    @PostMapping
    public ResponseEntity<Categoria> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto JSON con los datos de la categoría",
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Ejemplo de categoría",
                                    value = "{ \"nombre\": \"Programación\" }"
                            )
                    )
            )
            @RequestBody Categoria categoria) {

        Categoria creado = categoriaService.save(categoria);
        return ResponseEntity.status(201).body(creado);
    }

    @Operation(
            summary = "Actualizar una categoría existente",
            description = "Actualiza el nombre de una categoría existente según su ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente"),
                    @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> update(
            @Parameter(description = "ID de la categoría a actualizar", example = "1")
            @PathVariable Long id,
            @RequestBody Categoria categoria) {

        Categoria existente = categoriaService.findById(id);
        existente.setNombre(categoria.getNombre());
        return ResponseEntity.ok(categoriaService.save(existente));
    }

    @Operation(
            summary = "Eliminar una categoría",
            description = "Elimina una categoría de la plataforma según su ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Categoría eliminada correctamente"),
                    @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la categoría a eliminar", example = "1")
            @PathVariable Long id) {

        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // =====================
    // CONSULTAS PERSONALIZADAS
    // =====================

    @Operation(
            summary = "Categorías con cursos disponibles",
            description = "Devuelve las categorías que tienen cursos con plazas disponibles",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping("/con-cursos-disponibles")
    public ResponseEntity<List<Categoria>> categoriasConCursosDisponibles() {
        return ResponseEntity.ok(categoriaService.categoriasConCursosDisponibles());
    }

    @Operation(
            summary = "Buscar categorías por nombre",
            description = "Busca categorías cuyo nombre contenga el texto indicado",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
                    @ApiResponse(responseCode = "404", description = "No se encontraron categorías")
            }
    )
    @GetMapping("/buscar")
    public ResponseEntity<List<Categoria>> buscarCategorias(
            @Parameter(description = "Texto a buscar en el nombre de la categoría", example = "Programación")
            @RequestParam String nombre) {

        return ResponseEntity.ok(categoriaService.buscarCategoriasPorNombre(nombre));
    }

    @Operation(
            summary = "Categorías con más de N cursos",
            description = "Devuelve las categorías que tienen más cursos de los indicados",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
            }
    )
    @GetMapping("/min-cursos/{cantidad}")
    public ResponseEntity<List<Categoria>> categoriasConMinCursos(
            @Parameter(description = "Número mínimo de cursos", example = "2")
            @PathVariable long cantidad) {

        return ResponseEntity.ok(categoriaService.categoriasConMasDeNCursos(cantidad));
    }

    @Operation(
            summary = "Contar cursos por categoría",
            description = "Devuelve cada categoría junto con el número de cursos asociados. Cada elemento del array es: [0]=Categoría, [1]=Número de cursos",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
            }
    )
    @GetMapping("/conteo-cursos")
    public ResponseEntity<List<Object[]>> contarCursosPorCategoria() {
        return ResponseEntity.ok(categoriaService.contarCursosPorCategoria());
    }

    @Operation(
            summary = "Categorías de un curso",
            description = "Devuelve las categorías asociadas a un curso específico",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
                    @ApiResponse(responseCode = "404", description = "Curso no encontrado")
            }
    )
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Categoria>> categoriasPorCurso(
            @Parameter(description = "ID del curso", example = "1")
            @PathVariable Long cursoId) {

        return ResponseEntity.ok(categoriaService.categoriasPorCurso(cursoId));
    }
}