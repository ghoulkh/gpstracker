package com.bka.gpstracker.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResetPasswordRequest {
    @NotBlank(message = "username is required!")
    private String username;
    @NotBlank(message = "code is required!")
    private String code;
    @NotBlank(message = "new pass is required!")
    private String newPassword;
}
