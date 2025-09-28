package org.jcr.entidades;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.jcr.enums.EspecialidadMedica;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@ToString(exclude = {"hospital", "medicos", "salas"}) // Evitar recursión total

public class Departamento implements Serializable {
    private final String nombre;
    private final EspecialidadMedica especialidad;

    @Setter // Solo hospital es mutable
    private Hospital hospital;
    private final List<Medico> medicos = new ArrayList<>();
    private final List<Sala> salas = new ArrayList<>(); // Constructor personalizado MANTENER

    public Departamento(String nombre, EspecialidadMedica especialidad) {
        this.nombre = validarString(nombre, "El nombre del departamento no puede ser nulo ni vacío");
        this.especialidad = Objects.requireNonNull(especialidad, "La especialidad no puede ser nula");
    }

    // MÉTODOS DE NEGOCIO CRÍTICOS - NO TOCAR
    public void agregarMedico(Medico medico) {
        if (medico != null && !medicos.contains(medico)) {
            medicos.add(medico);
            medico.setDepartamento(this);
        }
    }

    public Sala crearSala(String numero, String tipo) {
        Sala sala = new Sala(numero, tipo, this);
        salas.add(sala);
        return sala;
    }

    // GETTERS INMUTABLES PERSONALIZADOS
    public List<Medico> getMedicos() {
        return Collections.unmodifiableList(medicos);
    }

    public List<Sala> getSalas() {
        return Collections.unmodifiableList(salas);
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
