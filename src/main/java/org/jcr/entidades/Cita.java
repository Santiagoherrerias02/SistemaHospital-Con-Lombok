package org.jcr.entidades;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.jcr.enums.EstadoCita;
import org.jcr.excepciones.CitaException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Getter // Solo getters automáticos para campos final
@ToString // ToString automático está bien

public class Cita implements Serializable {
    private final Paciente paciente;
    private final Medico medico;
    private final Sala sala;
    private final LocalDateTime fechaHora;
    private final BigDecimal costo;

    @Setter // Solo estos 2 campos necesitan setters
    private EstadoCita estado;

    @Setter
    private String observaciones;

    // CONSTRUCTOR CRÍTICO - NO CAMBIAR NI UNA LÍNEA
    public Cita(Paciente paciente, Medico medico, Sala sala, LocalDateTime fechaHora, BigDecimal costo) {
        this.paciente = Objects.requireNonNull(paciente, "El paciente no puede ser nulo");
        this.medico = Objects.requireNonNull(medico, "El médico no puede ser nulo");
        this.sala = Objects.requireNonNull(sala, "La sala no puede ser nula");
        this.fechaHora = Objects.requireNonNull(fechaHora, "La fecha y hora no pueden ser nulas");
        this.costo = Objects.requireNonNull(costo, "El costo no puede ser nulo");
        this.estado = EstadoCita.PROGRAMADA;
        this.observaciones = "";
    }

    // MÉTODOS CSV CRÍTICOS - NO TOCAR ABSOLUTAMENTE NADA
    public String toCsvString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s",
                paciente.getDni(),
                medico.getDni(),
                sala.getNumero(),
                fechaHora.toString(),
                costo.toString(),
                estado.name(),
                observaciones.replaceAll(",", ";"));
    }

    public static Cita fromCsvString(String csvString,
                                     Map<String, Paciente> pacientes,
                                     Map<String, Medico> medicos,
                                     Map<String, Sala> salas) throws CitaException {
        String[] values = csvString.split(",");
        if (values.length != 7) {
            throw new CitaException("Formato de CSV inválido para Cita: " + csvString);
        }

        String dniPaciente = values[0];
        String dniMedico = values[1];
        String numeroSala = values[2];
        LocalDateTime fechaHora = LocalDateTime.parse(values[3]);
        BigDecimal costo = new BigDecimal(values[4]);
        EstadoCita estado = EstadoCita.valueOf(values[5]);
        String observaciones = values[6].replaceAll(";", ",");

        Paciente paciente = pacientes.get(dniPaciente);
        Medico medico = medicos.get(dniMedico);
        Sala sala = salas.get(numeroSala);

        if (paciente == null) {
            throw new CitaException("Paciente no encontrado: " + dniPaciente);
        }
        if (medico == null) {
            throw new CitaException("Médico no encontrado: " + dniMedico);
        }
        if (sala == null) {
            throw new CitaException("Sala no encontrada: " + numeroSala);
        }

        Cita cita = new Cita(paciente, medico, sala, fechaHora, costo);
        cita.setEstado(estado);
        cita.setObservaciones(observaciones);

        return cita;
    }

    // SETTERS PERSONALIZADOS CON VALIDACIÓN - MANTENER
    public void setEstado(EstadoCita estado) {
        this.estado = Objects.requireNonNull(estado, "El estado no puede ser nulo");
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones != null ? observaciones : "";
    }
}
