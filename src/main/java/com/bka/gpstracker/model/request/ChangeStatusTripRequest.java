package com.bka.gpstracker.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChangeStatusTripRequest {
    @NotBlank(message = "status is required!")
    private String status;
    @NotBlank(message = "tripId is required!")
    private String tripId;
}
