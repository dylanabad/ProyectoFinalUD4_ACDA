package repository;

import model.Inscripciones;
import model.InscripcionesId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscripcionesRepository extends JpaRepository<Inscripciones, InscripcionesId> {
}