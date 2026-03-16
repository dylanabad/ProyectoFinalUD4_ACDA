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

@Tag(name = "Categorías", description = "Gestión de categorías de cursos")
@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    // =====================
    // CRUD
    // =====================

    @Operation(summary = "Obtener todas las categorías",
            description = "Devuelve una lista con todas las categorías disponibles en la plataforma")
    @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<Categoria>> getAll() {
        return ResponseEntity.ok(categoriaService.findAll());
    }

    @Operation(summary = "Buscar categoría por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> getById(
            @Parameter(description = "ID de la categoría")
            @PathVariable Long id) {

        return ResponseEntity.ok(categoriaService.findById(id));
    }

    @Operation(summary = "Crear nueva categoría")
    @ApiResponse(responseCode = "201", description = "Categoría creada correctamente")
    @PostMapping
    public ResponseEntity<Categoria> create(@RequestBody Categoria categoria) {
        Categoria creado = categoriaService.save(categoria);
        return ResponseEntity.status(201).body(creado);
    }

    @Operation(summary = "Actualizar una categoría existente")
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> update(
            @Parameter(description = "ID de la categoría a actualizar")
            @PathVariable Long id,
            @RequestBody Categoria categoria) {

        Categoria existente = categoriaService.findById(id);
        existente.setNombre(categoria.getNombre());
        return ResponseEntity.ok(categoriaService.save(existente));
    }

    @Operation(summary = "Eliminar una categoría")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la categoría a eliminar")
            @PathVariable Long id) {

        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // =====================
    // CONSULTAS PERSONALIZADAS
    // =====================

    @Operation(summary = "Categorías con cursos disponibles",
            description = "Devuelve las categorías que tienen cursos con plazas disponibles")
    @GetMapping("/con-cursos-disponibles")
    public ResponseEntity<List<Categoria>> categoriasConCursosDisponibles() {
        return ResponseEntity.ok(categoriaService.categoriasConCursosDisponibles());
    }

    @Operation(summary = "Buscar categorías por nombre",
            description = "Busca categorías cuyo nombre contenga el texto indicado")
    @GetMapping("/buscar")
    public ResponseEntity<List<Categoria>> buscarCategorias(
            @Parameter(description = "Texto a buscar en el nombre de la categoría")
            @RequestParam String nombre) {

        return ResponseEntity.ok(categoriaService.buscarCategoriasPorNombre(nombre));
    }

    @Operation(summary = "Categorías con más de N cursos")
    @GetMapping("/min-cursos/{cantidad}")
    public ResponseEntity<List<Categoria>> categoriasConMinCursos(
            @Parameter(description = "Número mínimo de cursos")
            @PathVariable long cantidad) {

        return ResponseEntity.ok(categoriaService.categoriasConMasDeNCursos(cantidad));
    }

    @Operation(summary = "Contar cursos por categoría",
            description = "Devuelve cada categoría junto con el número de cursos asociados")
    @GetMapping("/conteo-cursos")
    public ResponseEntity<List<Object[]>> contarCursosPorCategoria() {
        return ResponseEntity.ok(categoriaService.contarCursosPorCategoria());
    }

    @Operation(summary = "Categorías de un curso")
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Categoria>> categoriasPorCurso(
            @Parameter(description = "ID del curso")
            @PathVariable Long cursoId) {

        return ResponseEntity.ok(categoriaService.categoriasPorCurso(cursoId));
    }
}