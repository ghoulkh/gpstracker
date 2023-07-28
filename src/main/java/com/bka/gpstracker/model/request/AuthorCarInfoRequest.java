package com.bka.gpstracker.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class AuthorCarInfoRequest {
    @NotBlank(message = "username is required!")
    private String username;
    @NotBlank(message = "rfid is required!")
    private String rfid;
    private List<String> activeAreas;
    @NotBlank(message = "drivingLicense is required!")
    private String drivingLicense;
    @NotBlank(message = "licensePlate is required!")
    private String licensePlate;
}
