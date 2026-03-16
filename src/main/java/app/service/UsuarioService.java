package app.service;

import lombok.RequiredArgsConstructor;
import app.model.Usuario;
import app.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // =====================
    // CRUD BÁSICO
    // =====================

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }

    // =====================
    // CONSULTAS PERSONALIZADAS
    // =====================

    // Usuarios inscritos en un curso específico
    public List<Usuario> usuariosPorCurso(Long cursoId) {
        return usuarioRepository.findUsuariosPorCurso(cursoId);
    }

    // Usuarios que no están inscritos en ningún curso
    public List<Usuario> usuariosSinInscripciones() {
        return usuarioRepository.findUsuariosSinInscripciones();
    }
}