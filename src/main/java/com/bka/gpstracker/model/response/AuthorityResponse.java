package com.bka.gpstracker.model.response;

import com.bka.gpstracker.entity.Authority;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class AuthorityResponse {
    private Long id;
    private Authority.Role role;

    public static AuthorityResponse from(Authority authority) {
        if (authority == null) return null;
        AuthorityResponse response = new AuthorityResponse();
        BeanUtils.copyProperties(authority, response);
        return response;
    }
}
