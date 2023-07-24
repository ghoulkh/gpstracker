package com.bka.gpstracker.model.request;

import com.bka.gpstracker.common.DeliveryStatus;
import com.bka.gpstracker.model.StatusHistory;
import com.bka.gpstracker.solr.entity.DeliveryInfo;
import com.bka.gpstracker.util.DeliveryIdGenerator;
import com.bka.gpstracker.util.Utils;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Data
public class NewDeliveryRequest {
    @NotNull(message = "fromAddress is required!")
    private String fromAddress;
    @NotNull(message = "toAddress is required!")
    private String toAddress;
    @NotNull(message = "driverUsername is required!")
    private String driverUsername;
    @NotNull(message = "fullNameReceiver is required!")
    private String fullNameReceiver;
    @NotNull(message = "phoneNumberReceiver is required!")
    private String phoneNumberReceiver;
    @NotNull(message = "emailReceiver is required!")
    private String emailReceiver;
    @NotNull(message = "senderFullName is required!")
    private String senderFullName;
    @NotNull(message = "senderEmail is required!")
    private String senderEmail;
    @NotNull(message = "fromLat is required!")
    private String fromLat;
    @NotNull(message = "fromLon is required!")
    private String fromLon;
    @NotNull(message = "toLat is required!")
    private String toLat;
    @NotNull(message = "toLon is required!")
    private String toLon;

    public DeliveryInfo toDeliveryInfo(String createdBy) {
        DeliveryInfo deliveryInfo = new DeliveryInfo();
        deliveryInfo.setId(DeliveryIdGenerator.geneId());
        Long currentTime = System.currentTimeMillis();
        deliveryInfo.setCreatedAt(currentTime);
        deliveryInfo.setLastUpdatedAt(currentTime);
        BeanUtils.copyProperties(this, deliveryInfo);
        deliveryInfo.setDeliveryStatus(DeliveryStatus.NEW);
        deliveryInfo.setCreatedBy(createdBy);
        List<StatusHistory> statusHistories = new LinkedList<>();
        StatusHistory statusHistory = new StatusHistory();
        statusHistory.setDeliveryStatus(DeliveryStatus.NEW);
        statusHistory.setCreatedAt(currentTime);
        statusHistories.add(statusHistory);
        deliveryInfo.setStatusHistories(statusHistories);
        deliveryInfo.setSearchData(Utils.geneDataSearchDelivery(deliveryInfo));
        return deliveryInfo;
    }
}
