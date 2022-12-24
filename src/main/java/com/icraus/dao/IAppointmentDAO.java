package com.icraus.dao;

import com.icraus.model.Appointment;
import com.icraus.model.Patient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public interface IAppointmentDAO {
    Flux<Appointment> findBetweenDateTime(OffsetDateTime startDate, OffsetDateTime endDate);
    Flux<Appointment> findByDateTime(OffsetDateTime dateTime);
    Flux<Appointment> findByPatient(String patient);

    Flux<Appointment> getPatientAppointmentsHistory(Long patientId);

    Mono<Appointment> createAppointment(OffsetDateTime dateTime, Long patientId);

    Mono<Appointment> cancelAppointment(Long appointmentId, String reason);
}
