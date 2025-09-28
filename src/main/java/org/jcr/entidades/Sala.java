package org.jcr.entidades;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@ToString(of = {"numero", "tipo"}) // Evitar referencia circular con departamento
@EqualsAndHashCode(of = {"numero"}) // Solo número identifica sala

public class Sala implements Serializable {
    private final String numero;
    private final String tipo;
    private final Departamento departamento;
    private final List<Cita> citas = new ArrayList<>(); // Constructor personalizado MANTENER

    public Sala(String numero, String tipo, Departamento departamento) {
        this.numero = validarString(numero, "El número de sala no puede ser nulo ni vacío");
        this.tipo = validarString(tipo, "El tipo de sala no puede ser nulo ni vacío");
        this.departamento = Objects.requireNonNull(departamento, "El departamento no puede ser nulo");
    }

    // MÉTODO DE NEGOCIO - NO TOCAR
    public void addCita(Cita cita) {
        this.citas.add(cita);
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
