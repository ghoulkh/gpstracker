package com.bka.gpstracker.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "enterprise")
public class Enterprise {
    @Id
    @Column(name = "enterprise_code")
    private String enterpriseCode;
    @Column(name = "enterprise_name")
    private String enterpriseName;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "last_updated_date")
    private Date lastUpdatedDate;
}
