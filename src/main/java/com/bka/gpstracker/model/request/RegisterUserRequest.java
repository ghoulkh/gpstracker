package com.bka.gpstracker.model.request;

import com.bka.gpstracker.entity.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class RegisterUserRequest {
    @NotBlank(message = "username is required!")
    private String username;
    @NotBlank(message = "password is required!")
    private String password;
    @NotBlank(message = "fullName is required!")
    private String fullName;
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "invalid email!")
    @NotBlank(message = "email is required!")
    private String email;
    @Pattern(regexp = "^[0-9\\-\\+]{9,15}$", message = "invalid phone number!")
    @NotBlank(message = "phone is required!")
    private String phone;
    private String avaUrl;

    public User toUser() {
        User user = new User();
        user.setUsername(this.username);
        user.setFullName(this.fullName);
        user.setEmail(this.email);
        user.setPhone(this.phone);
        user.setAvaUrl(this.avaUrl);
        return user;
    }
}

