package app.controller;

import lombok.RequiredArgsConstructor;
import app.model.Curso;
import app.service.CursoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<Curso>> getAll() {
        return ResponseEntity.ok(cursoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Curso> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cursoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Curso> create(@RequestBody Curso curso) {
        Curso creado = cursoService.save(curso);
        return ResponseEntity.status(201).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Curso> update(@PathVariable Long id, @RequestBody Curso curso) {
        Curso existente = cursoService.findById(id);
        existente.setTitulo(curso.getTitulo());
        existente.setDescripcion(curso.getDescripcion());
        existente.setPlazasDisponibles(curso.getPlazasDisponibles());
        existente.setCategoria(curso.getCategoria());
        return ResponseEntity.ok(cursoService.save(existente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cursoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // 🔥 Endpoint de inscripción
    @PostMapping("/{cursoId}/inscribir/{usuarioId}")
    public ResponseEntity<String> inscribirUsuario(@PathVariable Long cursoId,
                                                   @PathVariable Long usuarioId) {
        cursoService.inscribirUsuario(cursoId, usuarioId);
        return ResponseEntity.ok("Usuario inscrito correctamente");
    }
}