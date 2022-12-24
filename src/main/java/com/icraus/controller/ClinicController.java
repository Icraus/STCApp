package com.icraus.controller;

import com.icraus.dao.IAppointmentDAO;
import com.icraus.model.Appointment;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@Controller
public class ClinicController {

    private final IAppointmentDAO iAppointmentDAO;

    public ClinicController(IAppointmentDAO iAppointmentDAO) {
        this.iAppointmentDAO = iAppointmentDAO;
    }
    @QueryMapping
    public Flux<Appointment> getTodayAppointments(){
        return iAppointmentDAO.findByDateTime(OffsetDateTime.now().withHour(0));
    }
    @QueryMapping
    public Flux<Appointment> getPatientAppointmentHistory(@Argument Long patientId){
        return iAppointmentDAO.getPatientAppointmentsHistory(patientId);
    }
    @QueryMapping
    public Flux<Appointment> getPatientAppointmentsByName(@Argument  String name){
        return iAppointmentDAO.findByPatient(name);
    }
    @QueryMapping
    public Flux<Appointment> getAppointmentsByDate(@Argument OffsetDateTime startDate, @Argument OffsetDateTime endDate){
        return iAppointmentDAO.findBetweenDateTime(startDate, endDate);
    }

    @MutationMapping
    public Mono<Appointment> createAppointment(@Argument OffsetDateTime dateTime,@Argument Long patientId){
        return iAppointmentDAO.createAppointment(dateTime, patientId);
    }

    @MutationMapping
    public Mono<Appointment> cancelAppointment(@Argument Long appointmentId, @Argument String reason){
        return iAppointmentDAO.cancelAppointment(appointmentId, reason);
    }

}
