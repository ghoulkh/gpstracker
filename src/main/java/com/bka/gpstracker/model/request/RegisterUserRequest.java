package com.bka.gpstracker.model.request;

import com.bka.gpstracker.entity.UserInfo;
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
    private String enterpriseCode;
    @Pattern(regexp = "^[0-9\\-\\+]{9,15}$", message = "invalid phone number!")
    @NotBlank(message = "phone is required!")
    private String phone;
    private String avaUrl;

    public UserInfo toUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(this.username);
        userInfo.setFullName(this.fullName);
        userInfo.setEmail(this.email);
        userInfo.setPhone(this.phone);
        userInfo.setAvaUrl(this.avaUrl);
        userInfo.setEnterpriseCode(this.enterpriseCode);
        return userInfo;
    }
}

