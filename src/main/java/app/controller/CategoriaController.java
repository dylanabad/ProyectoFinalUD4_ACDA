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
}