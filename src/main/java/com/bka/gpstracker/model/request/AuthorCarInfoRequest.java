package com.bka.gpstracker.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthorCarInfoRequest {
    @NotBlank(message = "username is required!")
    private String username;
    @NotBlank(message = "rfid is required!")
    private String rfid;
}
