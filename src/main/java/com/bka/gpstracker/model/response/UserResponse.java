package com.bka.gpstracker.model.response;

import com.bka.gpstracker.solr.entity.Authority;
import com.bka.gpstracker.solr.entity.UserInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;
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
    private Boolean isBusy;
    private String currentTripId;
    private List<AuthorityResponse> authorities;
    public static UserResponse from(UserInfo userInfo, List<Authority> authorities) {
        UserResponse result = new UserResponse();
        BeanUtils.copyProperties(userInfo, result);
        if (authorities != null)
            result.setAuthorities(authorities.stream()
                    .map(authority -> AuthorityResponse.from(authority))
                    .collect(Collectors.toList()));
        return result;
    }
}
