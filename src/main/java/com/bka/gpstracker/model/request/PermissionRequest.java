package com.bka.gpstracker.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class PermissionRequest {
    @NotBlank(message = "role is required!")
    @Pattern(regexp = "^ROLE_ADMIN$", message = "role invalid!")
    private String role;
    @NotBlank(message = "username is required!")
    private String username;
}
