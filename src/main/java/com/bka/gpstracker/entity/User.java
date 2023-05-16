package com.bka.gpstracker.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "user_tracker")
public class User {
    @Id
    @Column(name = "username", nullable = true)
    private String username;
    private Date createdDate = new Date();
    private Date lastModifiedDate = new Date();
    private String password;
    private String fullName;
    private String avaUrl;
    private String email;
    private String phone;
    private String createdBy;
}
