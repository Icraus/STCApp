package com.icraus.config;

import com.icraus.model.Appointment;
import com.icraus.model.AppointmentStatus;
import com.icraus.model.Patient;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class Initializer implements CommandLineRunner {

    @Autowired
    private Mutiny.SessionFactory sessionFactory;
    @Override
    public void run(String... args) throws Exception {

        sessionFactory.withTransaction(
                (statelessSession, tx) -> {
                    Patient patient = new Patient();
                    patient.setName("Ahmed");

                    Appointment appointment = new Appointment();
                    appointment.setStatus(AppointmentStatus.CANCELED);
                    appointment.setDateTime(OffsetDateTime.now());
                    appointment.setPatient(patient);
                    return statelessSession.persistAll(patient, appointment);
                }
        ).await().indefinitely();
    }
}
