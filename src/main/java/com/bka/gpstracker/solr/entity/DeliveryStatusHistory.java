package com.bka.gpstracker.solr.entity;

import lombok.Data;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import javax.persistence.Id;

@Data
@SolrDocument(solrCoreName = "delivery_status_history")
public class DeliveryStatusHistory {
    @Id
    @Indexed(name = "id", type = "string")
    private String id;
    private String ada;

}
