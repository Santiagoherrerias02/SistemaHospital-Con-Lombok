package org.jcr.entidades;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jcr.enums.TipoSangre;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@EqualsAndHashCode(callSuper = true) // Solo hereda de Persona (DNI)
@ToString(callSuper = true, of = {"telefono"})

public class Paciente extends Persona implements Serializable {
    private final HistoriaClinica historiaClinica;
    private final String telefono;
    private final String direccion;

    @Setter // Solo hospital es mutable
    private Hospital hospital;
    private final List<Cita> citas = new ArrayList<>(); // Constructor personalizado - crea HistoriaClinica automáticamente

    public Paciente(String nombre, String apellido, String dni, LocalDate fechaNacimiento, TipoSangre tipoSangre, String telefono, String direccion) {
        super(nombre, apellido, dni, fechaNacimiento, tipoSangre);
        this.telefono = validarString(telefono, "El teléfono no puede ser nulo ni vacío");
        this.direccion = validarString(direccion, "La dirección no puede ser nula ni vacía");
        this.historiaClinica = new HistoriaClinica(this); // CRÍTICO
    }

    // MÉTODOS DE NEGOCIO - NO TOCAR
    public void addCita(Cita cita) {
        this.citas.add(cita);
    }

    public List<Cita> getCitas() {
        return Collections.unmodifiableList(new ArrayList<>(citas));
    }

    // VALIDACIÓN - NO TOCAR
    private String validarString(String valor, String mensajeError) {
        Objects.requireNonNull(valor, mensajeError);
        if (valor.trim().isEmpty()) {
            throw new IllegalArgumentException(mensajeError);
        }
        return valor;
    }
}