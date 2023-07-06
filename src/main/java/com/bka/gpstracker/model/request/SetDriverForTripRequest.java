package com.bka.gpstracker.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SetDriverForTripRequest {
    @NotBlank(message = "driver is required!")
    private String driver;
    @NotBlank(message = "tripId is required!")
    private String tripId;
}
