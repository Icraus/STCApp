package com.icraus.dao;

import com.icraus.model.Appointment;
import com.icraus.model.AppointmentStatus;
import com.icraus.model.Patient;
import io.smallrye.mutiny.converters.uni.UniReactorConverters;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@Service
public class AppointmentDAOImpl implements IAppointmentDAO {

    @Autowired
    private Mutiny.SessionFactory sessionFactory;

    @Override
    public Flux<Appointment> findBetweenDateTime(OffsetDateTime startDate, OffsetDateTime endDate) {
        return this.sessionFactory.withStatelessTransaction(
                (session) -> session.createQuery(
                        "select a from Appointment a where a.dateTime between :startDate and :endDate",
                        Appointment.class
                ).setParameter("startDate", startDate).setParameter("endDate", endDate)
                        .getResultList())
                .convert()
                .with(UniReactorConverters.toMono())
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Flux<Appointment> findByDateTime(OffsetDateTime dateTime) {
        return findBetweenDateTime(dateTime.withHour(0), dateTime.withHour(23));
    }

    @Override
    public Flux<Appointment> findByPatient(String patient) {
        return this.sessionFactory.withStatelessTransaction(
                (session) -> session.createQuery(
                        "select a from Appointment a where a.patient.name like :name" , Appointment.class )
                        .setParameter("name", "%" + patient + "%")
                        .getResultList()
        )
                .convert()
                .with(UniReactorConverters.toMono())
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Flux<Appointment> getPatientAppointmentsHistory(Long patientId) {
        return this.sessionFactory.withStatelessTransaction(
                (session) -> session.createQuery(
                        "select a from Appointment a where a.patient.id = :patientId" , Appointment.class )
                        .setParameter("patientId", patientId)
                        .getResultList()
        )
                .convert()
                .with(UniReactorConverters.toMono())
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<Appointment> createAppointment(OffsetDateTime dateTime, Long patientId) {
        return this.sessionFactory.withStatelessTransaction(
                (session) -> session.get(Patient.class, patientId).flatMap(
                        patient -> {
                            Appointment appointment = new Appointment();
                            appointment.setDateTime(dateTime);
                            appointment.setPatient(patient);
                            appointment.setStatus(AppointmentStatus.SCHEDULED);
                            return session.insert(appointment).map(e -> appointment);
                        }
                    ))
                .convert()
                .with(UniReactorConverters.toMono());
    }
    @Override
    public Mono<Appointment> cancelAppointment(Long appointmentId, String reason) {
        return this.sessionFactory.withSession(
                (session) -> session.find(Appointment.class, appointmentId).flatMap(
                        appointment -> {
                            appointment.setStatus(AppointmentStatus.CANCELED);
                            appointment.setComment(reason);
                            return session.merge(appointment).map(e -> appointment);
                        }
                ))
                .convert()
                .with(UniReactorConverters.toMono());
    }

}
