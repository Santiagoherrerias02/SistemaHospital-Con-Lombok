package org.jcr.entidades;

import lombok.*;

import org.jcr.enums.EspecialidadMedica;
import org.jcr.enums.TipoSangre;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@EqualsAndHashCode(callSuper = true, of = {"matricula"}) // Incluye Persona + matricula
@ToString(callSuper = true, of = {"matricula", "especialidad"})

public class Medico extends Persona implements Serializable {
    private final Matricula matricula;
    private final EspecialidadMedica especialidad;

    @Setter // Solo departamento es mutable
    private Departamento departamento;
    private final List<Cita> citas = new ArrayList<>(); // Constructor personalizado MANTENER EXACTO

    public Medico(String nombre, String apellido, String dni, LocalDate fechaNacimiento, TipoSangre tipoSangre, String numeroMatricula, EspecialidadMedica especialidad) {
        super(nombre, apellido, dni, fechaNacimiento, tipoSangre);
        this.matricula = new Matricula(numeroMatricula);
        this.especialidad = Objects.requireNonNull(especialidad, "La especialidad no puede ser nula");
    }

    // MÉTODOS DE NEGOCIO - NO TOCAR
    public void addCita(Cita cita) {
        this.citas.add(cita);
    }

    // Getter personalizado para lista inmutable
    public List<Cita> getCitas() {
        return Collections.unmodifiableList(new ArrayList<>(citas));
    }
}