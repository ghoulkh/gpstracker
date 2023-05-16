package com.bka.gpstracker.model.response;

import com.bka.gpstracker.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.stream.Collectors;

@Data
public class UserResponse {
    private String username;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdDate;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date lastModifiedDate;
    private String fullName;
    private String avaUrl;
    private String email;
    private String createdBy;
    public static UserResponse from(User user) {
        UserResponse result = new UserResponse();
        BeanUtils.copyProperties(user, result);
        return result;
    }
}
