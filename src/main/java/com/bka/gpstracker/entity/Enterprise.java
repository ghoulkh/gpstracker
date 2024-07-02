package com.bka.gpstracker.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
public class Enterprise {
    @Id
    private String enterpriseCode;
    private String enterpriseName;
    private Date createdDate;
    private Date lastUpdatedDate;
}
