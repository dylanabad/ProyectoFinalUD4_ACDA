package app.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "usuario")
public class Usuario {

    @OneToMany(mappedBy = "usuario")
    @JsonManagedReference
    private List<Inscripciones> inscripciones;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100)
    @NotNull
    private String nombre;

    @Size(max = 150)
    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private LocalDate fechaRegistro;
}