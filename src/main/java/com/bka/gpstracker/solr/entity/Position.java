package com.bka.gpstracker.solr.entity;

import lombok.Data;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import javax.persistence.Id;
import java.util.Date;

@Data
@SolrDocument(solrCoreName = "position_log")
public class Position {
    @Id
    @Indexed(name = "id", type = "string")
    private String id;
    @Indexed(name = "createdAt", type = "long")
    private Long createdAt;
    @Indexed(name = "rfid", type = "string")
    private String rfid;
    @Indexed(name = "lat", type = "string")
    private String lat;
    @Indexed(name = "lon", type = "string")
    private String lon;
}
