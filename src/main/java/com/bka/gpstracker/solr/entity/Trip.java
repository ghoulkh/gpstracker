package com.bka.gpstracker.solr.entity;

import lombok.Data;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import org.springframework.data.annotation.Id;
import java.util.Date;

@Data
@SolrDocument(solrCoreName = "trip")
public class Trip {
    @Id
    @Indexed(name = "id", type = "string")
    private String id;
    @Indexed(name = "fromAddress", type = "string")
    private String fromAddress;
    @Indexed(name = "toAddress", type = "string")
    private String toAddress;
    @Indexed(name = "fromLat", type = "string")
    private String fromLat;
    @Indexed(name = "fromLon", type = "string")
    private String fromLon;
    @Indexed(name = "toLat", type = "string")
    private String toLat;
    @Indexed(name = "toLon", type = "string")
    private String toLon;
    @Indexed(name = "notes", type = "string")
    private String notes;
    @Indexed(name = "createdBy", type = "string")
    private String createdBy;
    @Indexed(name = "createdAt", type = "date")
    private Date createdAt;
    @Indexed(name = "status", type = "string")
    private String status;
    @Indexed(name = "driver", type = "string")
    private String driver;
}
