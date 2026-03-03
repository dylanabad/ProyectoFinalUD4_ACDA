package app.repository;

import app.model.Inscripciones;
import app.model.InscripcionesId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscripcionesRepository extends JpaRepository<Inscripciones, InscripcionesId> {
}