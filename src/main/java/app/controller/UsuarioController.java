package app.controller;

import lombok.RequiredArgsConstructor;
import app.model.Usuario;
import app.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@Tag(name = "Usuarios", description = "Gestión de usuarios registrados en la plataforma")
@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // =====================
    // CRUD
    // =====================

    @Operation(
            summary = "Obtener todos los usuarios",
            description = "Devuelve la lista completa de usuarios registrados en la plataforma",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado de usuarios obtenido correctamente"),
                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
            }
    )
    @GetMapping
    public ResponseEntity<List<Usuario>> getAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @Operation(
            summary = "Buscar usuario por ID",
            description = "Devuelve un usuario según su ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(
            @Parameter(description = "ID del usuario", example = "2")
            @PathVariable Long id) {

        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @Operation(
            summary = "Crear nuevo usuario",
            description = "Crea un usuario con los datos proporcionados",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuario creado correctamente"),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos")
            }
    )
    @PostMapping
    public ResponseEntity<Usuario> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto JSON con los datos del usuario",
                    required = true,
                    content = @Content(
                            examples = @ExampleObject(
                                    name = "Ejemplo usuario",
                                    value = "{ \"nombre\": \"Dylan\", \"email\": \"dylan@mail.com\", \"fechaRegistro\": \"2026-03-16\" }"
                            )
                    )
            )
            @RequestBody Usuario usuario) {

        Usuario creado = usuarioService.save(usuario);
        return ResponseEntity.status(201).body(creado);
    }

    @Operation(
            summary = "Actualizar usuario",
            description = "Actualiza los datos de un usuario existente según su ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(
            @Parameter(description = "ID del usuario a actualizar", example = "2")
            @PathVariable Long id,
            @RequestBody Usuario usuario) {

        Usuario existente = usuarioService.findById(id);
        existente.setNombre(usuario.getNombre());
        existente.setEmail(usuario.getEmail());
        existente.setFechaRegistro(usuario.getFechaRegistro());

        return ResponseEntity.ok(usuarioService.save(existente));
    }

    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina un usuario según su ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del usuario", example = "2")
            @PathVariable Long id) {

        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // =====================
    // CONSULTAS PERSONALIZADAS
    // =====================

    @Operation(
            summary = "Usuarios inscritos en un curso",
            description = "Devuelve la lista de usuarios que están inscritos en un curso específico",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
                    @ApiResponse(responseCode = "404", description = "Curso no encontrado")
            }
    )
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Usuario>> usuariosPorCurso(
            @Parameter(description = "ID del curso", example = "1")
            @PathVariable Long cursoId) {

        return ResponseEntity.ok(usuarioService.usuariosPorCurso(cursoId));
    }

    @Operation(
            summary = "Usuarios sin inscripciones",
            description = "Devuelve la lista de usuarios que no están inscritos en ningún curso",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
            }
    )
    @GetMapping("/sin-inscripciones")
    public ResponseEntity<List<Usuario>> usuariosSinInscripciones() {
        return ResponseEntity.ok(usuarioService.usuariosSinInscripciones());
    }
}