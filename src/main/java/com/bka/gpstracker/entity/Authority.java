package com.bka.gpstracker.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "authority")
public class Authority implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ManyToOne()
    @JoinColumn(name = "username")
    private UserInfo userInfo;

    public enum Role {
        ROLE_ADMIN("ROLE_ADMIN"),
        ROLE_USER("ROLE_USER"),
        ROLE_ENTERPRISE_ADMIN("ROLE_ENTERPRISE_ADMIN");
        private String value;
        private Role(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}

