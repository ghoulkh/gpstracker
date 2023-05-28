package com.bka.gpstracker.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "user_tracker")
public class UserInfo {
    @Id
    @Column(name = "username", nullable = true)
    private String username;
    @Column(name = "enterprise_code")
    private String enterpriseCode;
    private Date createdDate = new Date();
    private Date lastModifiedDate = new Date();
    private String password;
    private String fullName;
    private String avaUrl;
    private String email;
    private String phone;
    private String createdBy;
    @OneToMany(mappedBy = "userInfo", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Authority> authorities;

    public User toUser() {
        User result = new User();
        result.setUsername(this.username);
        result.setPassword("fake-password");
        return result;
    }

}
