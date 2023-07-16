package com.bka.gpstracker.model.request;

import com.bka.gpstracker.common.DeliveryStatus;
import com.bka.gpstracker.solr.entity.DeliveryInfo;
import com.bka.gpstracker.util.Utils;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotNull;
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

    public DeliveryInfo toDeliveryInfo(String createdBy) {
        DeliveryInfo deliveryInfo = new DeliveryInfo();
        deliveryInfo.setId(UUID.randomUUID().toString());
        Long currentTime = System.currentTimeMillis();
        deliveryInfo.setCreatedAt(currentTime);
        deliveryInfo.setLastUpdatedAt(currentTime);
        BeanUtils.copyProperties(this, deliveryInfo);
        deliveryInfo.setDeliveryStatus(DeliveryStatus.NEW);
        deliveryInfo.setCreatedBy(createdBy);
        deliveryInfo.setSearchData(Utils.geneDataSearchDelivery(deliveryInfo));
        return deliveryInfo;
    }
}
