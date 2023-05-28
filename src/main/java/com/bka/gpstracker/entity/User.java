package com.bka.gpstracker.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "[User]")
public class User {
    @Id
    @Column(name = "UserName")
    private String username;
    @Column(name = "PassWord")
    private String password;
}
