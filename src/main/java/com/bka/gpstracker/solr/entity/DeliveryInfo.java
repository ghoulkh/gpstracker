package com.bka.gpstracker.solr.entity;

import com.bka.gpstracker.common.DeliveryStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Data
@SolrDocument(solrCoreName = "delivery-info")
public class DeliveryInfo {
    @Id
    @Indexed(name = "id", type = "string")
    private String id;
    @Indexed(name = "createdAt", type = "long")
    private Long createdAt;
    @Indexed(name = "lastUpdatedAt", type = "long")
    private Long lastUpdatedAt;
    @Indexed(name = "fromAddress", type = "string")
    private String fromAddress;
    @Indexed(name = "toAddress", type = "string")
    private String toAddress;
    @Indexed(name = "driverUsername", type = "string")
    private String driverUsername;
    @Indexed(name = "fullNameReceiver", type = "string")
    private String fullNameReceiver;
    @Indexed(name = "phoneNumberReceiver", type = "string")
    private String phoneNumberReceiver;
    @Indexed(name = "emailReceiver", type = "string")
    private String emailReceiver;
    @JsonIgnore
    @Indexed(name = "searchData", type = "string")
    private String searchData;
    @Indexed(name = "createdBy", type = "string")
    private String createdBy;
    @Indexed(name = "status", type = "string")
    @Enumerated(value = EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Indexed(name = "senderFullName", type = "string")
    private String senderFullName;
    @Indexed(name = "senderEmail", type = "string")
    private String senderEmail;
}
