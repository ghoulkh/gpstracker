package com.bka.gpstracker.solr.entity;

import com.bka.gpstracker.entity.User;
import com.bka.gpstracker.util.Utils;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;

@Data
@SolrDocument(solrCoreName = "user")
public class UserInfo {
    @Id
    @Indexed(name = "username", type = "string")
    private String username;
    @Indexed(name = "createdDate", type = "date")
    private Date createdDate = new Date();
    @Indexed(name = "lastCheckInAt", type = "date")
    private Date lastCheckInAt;
    @Indexed(name = "lastModifiedDate", type = "date")
    private Date lastModifiedDate = new Date();
    @Indexed(name = "password", type = "string")
    private String password;
    @Indexed(name = "fullName", type = "string")
    private String fullName;
    @Indexed(name = "avaUrl", type = "string")
    private String avaUrl;
    @Indexed(name = "email", type = "string")
    private String email;
    @Indexed(name = "phone", type = "string")
    private String phone;
    @Indexed(name = "createdBy", type = "string")
    private String createdBy;
    @Indexed(name = "isBusy", type = "boolean")
    private Boolean isBusy;
    @Indexed(name = "currentTripId", type = "string")
    private String currentTripId;
    @Indexed(name = "status", type = "string")
    private String status;
    public Boolean getIsBusy() {
        return this.isBusy != null && isBusy;
    }

    public User toUser() {
        User result = new User();
        result.setUsername(this.username);
        result.setPassword("fake-password");
        return result;
    }

}
