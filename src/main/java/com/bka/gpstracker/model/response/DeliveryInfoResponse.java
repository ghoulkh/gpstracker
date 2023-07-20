package com.bka.gpstracker.model.response;

import com.bka.gpstracker.common.DeliveryStatus;
import com.bka.gpstracker.model.StatusHistory;
import com.bka.gpstracker.solr.entity.DeliveryInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.LinkedList;
import java.util.List;

@Data
public class DeliveryInfoResponse {
    private String id;
    private Long createdAt;
    private Long lastUpdatedAt;
    private String fromAddress;
    private String fromLat;
    private String fromLon;
    private String toLat;
    private String toLon;
    private String toAddress;
    private String driverUsername;
    private String fullNameReceiver;
    private String phoneNumberReceiver;
    private String emailReceiver;
    private String createdBy;
    @Enumerated(value = EnumType.STRING)
    private DeliveryStatus deliveryStatus;
    @JsonIgnoreProperties
    private List<StatusHistory> statusHistories;
    private String senderFullName;
    private String senderEmail;

    public static DeliveryInfoResponse from(DeliveryInfo deliveryInfo) {
        DeliveryInfoResponse deliveryInfoResponse = new DeliveryInfoResponse();
        BeanUtils.copyProperties(deliveryInfo, deliveryInfoResponse);
        List<StatusHistory> statusHistories = new LinkedList<>();
        if (deliveryInfo.getStatusHistories() != null) {
            for (StatusHistory statusHistory : deliveryInfo.getStatusHistories()) {
                StatusHistory statusHistory1 = new StatusHistory();
                statusHistory1.setDeliveryStatus(statusHistory.getDeliveryStatus());
                statusHistory1.setCreatedAt(statusHistory.getCreatedAt());
                statusHistories.add(statusHistory1);
            }
        }
        deliveryInfoResponse.setStatusHistories(statusHistories);
        return deliveryInfoResponse;
    }
}
