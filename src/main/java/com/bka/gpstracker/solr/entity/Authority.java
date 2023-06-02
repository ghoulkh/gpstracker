package com.bka.gpstracker.solr.entity;

import lombok.Data;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import javax.persistence.*;

@Data
@SolrDocument(solrCoreName = "authority")
public class Authority {

    @Id
    @Indexed(name = "id", type = "string")
    private String id;
    @Enumerated(EnumType.STRING)
    @Indexed(name = "role", type = "string")
    private Role role;
    @Indexed(name = "username", type = "string")
    private String username;

    public enum Role {
        ROLE_ADMIN("ROLE_ADMIN"),
        ROLE_USER("ROLE_USER"),
        ROLE_DRIVER("ROLE_DRIVER");
        private String value;
        private Role(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}

