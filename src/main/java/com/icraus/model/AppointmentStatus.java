package com.icraus.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AppointmentStatus {
    @JsonProperty("SCHEDULED")
    SCHEDULED,
    @JsonProperty("CANCELED")
    CANCELED,
    @JsonProperty("COMPLETED")
    COMPLETED
}
