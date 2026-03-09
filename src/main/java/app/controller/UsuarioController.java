package app.controller;

import lombok.RequiredArgsConstructor;
import app.model.Usuario;
import app.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "Usuarios", description = "Gestión de usuarios registrados en la plataforma")
@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Obtener todos los usuarios")
    @GetMapping
    public ResponseEntity<List<Usuario>> getAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @Operation(summary = "Buscar usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getById(
            @Parameter(description = "ID del usuario")
            @PathVariable Long id) {

        return ResponseEntity.ok(usuarioService.findById(id));
    }

    @Operation(summary = "Crear nuevo usuario")
    @PostMapping
    public ResponseEntity<Usuario> create(@RequestBody Usuario usuario) {
        Usuario creado = usuarioService.save(usuario);
        return ResponseEntity.status(201).body(creado);
    }

    @Operation(summary = "Actualizar usuario")
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(
            @Parameter(description = "ID del usuario")
            @PathVariable Long id,
            @RequestBody Usuario usuario) {

        Usuario existente = usuarioService.findById(id);
        existente.setNombre(usuario.getNombre());
        existente.setEmail(usuario.getEmail());
        existente.setFechaRegistro(usuario.getFechaRegistro());

        return ResponseEntity.ok(usuarioService.save(existente));
    }

    @Operation(summary = "Eliminar usuario")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID del usuario")
            @PathVariable Long id) {

        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}