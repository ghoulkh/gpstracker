package com.bka.gpstracker.solr.entity;

import lombok.Data;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;
import org.springframework.data.annotation.Id;

@SolrDocument(solrCoreName = "position")
@Data
public class CurrentPosition {
    @Id
    @Indexed(name = "username", type = "string")
    private String username;
    @Indexed(name = "lat", type = "string")
    private String lat;
    @Indexed(name = "lon", type = "string")
    private String lon;
}
